package game.pieces;

import game.Game;
import game.util.Team;

import java.util.ArrayList;

public class King extends Piece {

	public King(Team t, int rank, int file) {
		super(t, rank, file);
	}
	public King(int rank, int file) {
		super(Team.WHITE, rank, file);
	}

	/*
	 * Returns a list of possible moves for the piece ignoring check
	 */
	public ArrayList<int[]> getPossibleMoves() {

		ArrayList<int[]> out = new ArrayList<>();

		for(int r = -1; r <= 1; r++){
			for(int f = -1; f <= 1; f++){
				if(r==0&&f==0)
					continue;
				int[] checking = new int[]{super.rank+r, super.file+f};
				Piece checkPiece;
				if (Game.inBounds(checking)) {
					checkPiece = Game.getSquare(checking);
					if(checkPiece == null) {
						out.add(checking);
					} else {
						if(checkPiece.getTeam()!=super.team)
							out.add(checking);
						break;
					}
				}
			}
		}

		return out;

		/* Old code for couldMoveTo(rank, file). Would return if a piece could move to a square ignoring check
		if (Game.getSquare(rank, file) != null && Game.getSquare(rank, file).getTeam() == super.team)
			return false;

		int dR = Math.abs(rank - super.rank);
		int dF = Math.abs(file - super.file);

		return dR<=1 && dF<=1;*/
	}

	public String toString(){
		return super.toString() + 'K';
	}
}
