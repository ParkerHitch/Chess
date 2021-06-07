package game;

import com.sun.jdi.VMOutOfMemoryException;
import game.pieces.*;
import game.util.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


//THE ONLY CLASS THE PLAYER WILL INTERACT WITH
public class Game {
	
	private static Piece[][] board;
	private static int move = 1;

	private static King whiteKing;
	private static King blackKing;

	private static Hashtable<Integer, ArrayList<int[]>> validMoves;
	public static void newGame() {
		move = 1;
		board = new Piece[][]
			{{new Rook(0,0), new Knight(0,1), new Bishop(0,2), new Queen(0,3), new King(0,4), new Bishop(0,5), new Knight(0,6), new Rook(0,7)},
			{new Pawn(1,0),new Pawn(1,1),new Pawn(1,2),new Pawn(1,3),new Pawn(1,4),new Pawn(1,5),new Pawn(1,6),new Pawn(1,7)},
			{null,null,null,null,null,null,null,null},
			{null,null,null,null,null,null,null,null},
			{null,null,null,null,null,null,null,null},
			{null,null,null,null,null,null,null,null},
			{new Pawn(Team.BLACK, 6, 0),new Pawn(Team.BLACK, 6, 1),new Pawn(Team.BLACK, 6, 2),new Pawn(Team.BLACK, 6, 3),new Pawn(Team.BLACK, 6, 4),new Pawn(Team.BLACK, 6, 5),new Pawn(Team.BLACK, 6, 6),new Pawn(Team.BLACK, 6, 7)},
			{new Rook(Team.BLACK, 7, 0), new Knight(Team.BLACK, 7, 1), new Bishop(Team.BLACK, 7, 2), new Queen(Team.BLACK, 7, 3), new King(Team.BLACK, 7, 4), new Bishop(Team.BLACK, 7, 5), new Knight(Team.BLACK, 7, 6), new Rook(Team.BLACK, 7, 7)}};
		whiteKing = (King) board[0][4];
		blackKing = (King) board[7][4];
		validMoves = getAllValidMoves();
	}
	
	public static void main(String[] args) {
		newGame();
		Scanner sc = new Scanner(System.in);
		boolean go = true;
		while (go){
			System.out.println(getBoardString());
			if(validMoves.isEmpty()) {
				if (isInCheck(getMovingTeam()==Team.WHITE?whiteKing.getPos():blackKing.getPos())) {
					System.out.println("Checkmate! "+(getMovingTeam()==Team.WHITE?"Black":"White")+" wins!");
				} else {
					System.out.println("Stalemate! No winners");
				}
				break;
			} else {
				System.out.println(getMovingTeam() + " to move");
			}
			System.out.print("Enter input: ");
			String in = sc.nextLine().toLowerCase(Locale.ROOT).trim();
			switch (in){
				case "exit":
				case "stop":
				case "quit":
					go = false;
					break;
				case "restart":
				case "new game":
					newGame();
					break;
				default:
					try {
						handleInput(in);
					} catch (InvalidInputException e){
						e.printStackTrace();
					}
			}
		}
	}

	public static void handleInput(String in) throws InvalidInputException {

		//INPUT FORMAT MUST BE: a1h8

		if(in.length()!=4)
			throw new InvalidInputException();

		int[] from = new int[]{(int)in.charAt(1) - (int)'1', ((int)in.charAt(0)) - (int)'a'};
		int[] to   = new int[]{(int)in.charAt(3) - (int)'1', ((int)in.charAt(2)) - (int)'a'};

		if(!(inBounds(to) && inBounds(from)))
			throw new InvalidInputException();

		Piece moving = getSquare(from);

		if(moving == null || moving.getTeam()!=getMovingTeam())
			throw new InvalidInputException();

		//CASTLING:
		//Player moving a king. king hasn't moved
		if(moving instanceof King && moving.getLastMoved()==0){
			int[] kPos = moving.getPos();
			//Staying on back rank & king isn't in check
			if(to[0] == kPos[0] && !isInCheck(kPos)){
				int dF = to[1] - kPos[1];
				//Moving king 2 left or right. Attempting to castle
				if(Math.abs(dF) == 2){
					//Find rook
					Piece rook = getSquare(kPos[0], dF<0?0:7);
					//Don't need to check color bc if black would have moved
					if(rook instanceof Rook && rook.getLastMoved()==0){
						//check if squares are empty
						if(getSquare(kPos[0], kPos[1]+dF/2)==null && getSquare(kPos[0], kPos[1]+dF)==null && (dF>0 || getSquare(kPos[0], kPos[1]-3)==null)) {
							//Check if king would end up in check or if goes through square in check
							if(!(isInCheck(new int[]{kPos[0], kPos[1] + dF / 2}) || isInCheck(new int[]{kPos[0], kPos[1] + dF}))){
								board[kPos[0]][kPos[1] + dF/2] = rook;
								board[rook.getPos()[0]][rook.getPos()[1]] = null;
								rook.move(kPos[0], kPos[1] + dF/2);
								makeMove(to, from);
								return;
							}
						}
					}
				}
			}
		}
		//EN PASSANT:
		//Moving a pawn, not moving to moving left/right 1, moving to empty square, and moving forward 1
		if(moving instanceof Pawn && Math.abs(to[1]-from[1])==1 && getSquare(to)==null && (to[0]-from[0])==(getMovingTeam()==Team.WHITE?1:-1)){
			Piece pawn = getSquare(from[0], to[1]);
			if(pawn instanceof Pawn && ((Pawn) pawn).getFirstMoved()==(move-1) && from[0]==(getMovingTeam()==Team.WHITE?4:3)) {
				Piece temp = board[from[0]][to[1]];
				board[from[0]][to[1]] = null;
				if (moveResultsInCheck(from, to)) {
					board[from[0]][to[1]] = temp;
					throw new InvalidInputException();
				}
				System.out.println("EN PASSANT!");
				makeMove(to, from);
				return;
			}
		}
		AtomicBoolean good = new AtomicBoolean(false);
		validMoves.forEach((square, moves) -> {
			for (int[] move : moves) {
				if (move[0] == to[0] && move[1] == to[1] && square / 8 == from[0] && square % 8 == from[1]) {
					good.set(true);
					break;
				}
			}
		});
		if (!good.get())
			throw new InvalidInputException();
		//Pawn promotion. New queen will be moved to 8th rank when makeMove is called. Cannot en passant into promotion.
		if(moving instanceof Pawn && to[0] == 8)
			board[from[0]][from[1]] = new Queen(getMovingTeam(), from[0], from[1]);

		//Make move on real board:
		makeMove(to, from);
	}

	public static Hashtable<Integer, ArrayList<int[]>> getAllValidMoves(){
		Hashtable<Integer, ArrayList<int[]>> validMoves = new Hashtable<>();
		for(int r = 0; r < 8; r++){
			for(int f = 0; f < 8; f++){
				if(getSquare(r,f)!=null && getSquare(r,f).getTeam()==getMovingTeam()){
					validMoves.put((r*8) + f, getSquare(r,f).getPossibleMoves());
				}
			}
		}

		ArrayList<Integer> toRemove = new ArrayList<>();
		validMoves.forEach((square, moves) -> {
			moves.removeIf(move -> moveResultsInCheck(new int[]{square/8,square%8}, move));
			if(moves.isEmpty())
				toRemove.add(square);
		});
		for (int sq: toRemove)
			validMoves.remove(sq);

		return validMoves;
	}

	private static boolean moveResultsInCheck(int[] from, int[] to){
		//Make move TEMPORARILY
		Piece temp = getSquare(to);
		board[to[0]][to[1]] = getSquare(from);
		board[from[0]][from[1]] = null;

		//King position
		int[] king;
		if(getSquare(from) instanceof King) {
			king = from;
		} else {
			king = getMovingTeam()==Team.WHITE ? whiteKing.getPos() : blackKing.getPos();
		}

		//Check if king is in check on board:
		if(isInCheck(king)) {
			//Undo temp
			board[from[0]][from[1]] = getSquare(to);
			board[to[0]][to[1]] = temp;
			return true;
		}

		//Undo temp:
		board[from[0]][from[1]] = getSquare(to);
		board[to[0]][to[1]] = temp;
		return false;
	}

	public static final int[] KnightR = {2, 1, -1, -2, -2, -1,  1,  2};
	public static final int[] KnightF = {1, 2,  2,  1, -1, -2, -2, -1};
	public static boolean isInCheck(int[] square){
		//Check for Bishop (& Queen). Also pawn bc diagonals
		for (int r = -1; r < 2; r+=2) {
			for(int f = -1; f < 2; f+=2) {
				//Cast out ray on diagonal until it hits a piece or edge
				int i = 1;
				int[] checking = new int[]{square[0]+i*r, square[1]+i*f};
				Piece checkPiece;
				while(inBounds(checking)) {
					checkPiece = getSquare(checking);
					if(checkPiece == null) {
						i++;
						checking[0] = square[0] + i * r;
						checking[1] = square[1] + i * f;
					} else if(checkPiece.getTeam()!=getMovingTeam()){
						if(checkPiece instanceof Bishop || checkPiece instanceof Queen)
							return true;
						//Pawn is next to (and diagonal to) square
						if(checkPiece instanceof Pawn && i==1){
							//Pawn is "facing" square
							//When white, r needs to be +1 bc that means we are checking up and black pawns can only go down
							if(r==(getMovingTeam()==Team.WHITE?1:-1))
								return true;
						}
					} else {
						break;
					}
				}
			}
		}

		//Check for Rook (& Queen)
		for (int r=-1; r < 2; r++){
			//Not checking on files unless not checking ranks
			for(int f = r==0?-1:0; f < 2; f+=2) {
				//Cast out ray until it hits a piece or edge
				int i = 1;
				int[] checking = new int[]{square[0]+i*r, square[1]+i*f};
				Piece checkPiece;
				while(inBounds(checking)) {
					checkPiece = getSquare(checking);
					if(checkPiece == null) {
						i++;
						checking[0] = square[0] + i * r;
						checking[1] = square[1] + i * f;
					} else if(checkPiece.getTeam()!=getMovingTeam() && (checkPiece instanceof Rook || checkPiece instanceof Queen)){
						return true;
					} else {
						break;
					}
				}
			}
		}

		//Check for Knight
		for (int i = 0; i < 8; i++) {
			int[] checking = new int[]{square[0]+KnightR[i], square[1]+KnightF[i]};
			if(inBounds(checking)) {
				Piece p = getSquare(checking);
				if(p instanceof Knight && p.getTeam()!=getMovingTeam())
					return true;
			}
		}

		return false;
	}

	public static boolean inBounds(int[] square){
		return square[0] <= 7 && square[0] >= 0 && square[1] <= 7 && square[1] >= 0;
	}

	public static void makeMove(int[] to, int[] from){
		getSquare(from).move(to);
		board[to[0]][to[1]] = getSquare(from);
		board[from[0]][from[1]] = null;
		move++;
		validMoves = getAllValidMoves();
	}

	public static int getMoveNumber() { return move; };
	public static Team getMovingTeam() { return move%2==1?Team.WHITE:Team.BLACK; }
	public static Piece getSquare(int rank, int file) { return board[rank][file]; }
	public static Piece getSquare(int[] square) { return board[square[0]][square[1]]; }
	public static String getBoardString() {
		StringBuilder out = new StringBuilder();
		for(int r=7; r>=0; r--) {
			out.append(r+1);
			for(int f=0; f<8; f++) {
				out.append((r + f) % 2 == 1 ? ConsoleColors.WHITE_BACKGROUND : ConsoleColors.BLACK_BACKGROUND).append(board[r][f] == null ? ' ' : board[r][f]);
			}
			out.append(ConsoleColors.RESET).append("\n");
		}
		return out.append(" abcdefgh").toString();
	}
}