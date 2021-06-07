package game.pieces;

import game.Game;
import game.util.Team;

import java.util.ArrayList;

public class Pawn extends Piece {

	private int firstMoved = 0;

	public Pawn(Team t, int rank, int file) {
		super(t, rank, file);
	}
	public Pawn(int rank, int file) {
		super(Team.WHITE, rank, file);
	}

	@Override
	public void move(int rank, int file) {
		super.move(rank, file);
		if(this.firstMoved==0)
			this.firstMoved = Game.getMoveNumber();
	}

	public int getFirstMoved(){
		return firstMoved;
	}
	/*
	 * Checks if a piece could move to a square ignoring checks
	 */
	public ArrayList<int[]> getPossibleMoves() {

		ArrayList<int[]> out = new ArrayList<>();

		int mDir = super.team==Team.WHITE?1:-1;

		//Moving forward
		if(Game.getSquare(super.rank+mDir,super.file)==null) {
			out.add(new int[]{super.rank + mDir, super.file});
			if(super.lastMoved==0 && Game.getSquare(super.rank+2*mDir,super.file)==null)
				out.add(new int[]{super.rank + 2*mDir, super.file});
		}

		//Capturing
		for(int f = -1; f <=1; f+=2){
			int[] checking = {super.rank+mDir, super.file+f};
			if(Game.inBounds(checking) && Game.getSquare(checking)!=null && Game.getSquare(checking).getTeam()!=this.team)
				out.add(checking);
		}

		//En passant in Game

		return out;

		/*//Can't capture own pieces
		if(Game.getSquare(rank, file) != null && Game.getSquare(rank, file).getTeam() == super.team)
			return false;
		
		//Multiplier to invert things for black

		//Only move forward
		if((super.team==Team.WHITE&&rank<=super.rank)||(super.team==Team.BLACK&&rank>=super.rank))
			return false;
		
		//Must be a capture to move left/right
		if(file != super.file) {
			//Moving 2+ squares L/R
			if(Math.abs(file - super.file)>1)
				return false;
			
			//Not moving forward or moving forward too many
			if(rank != super.rank + (super.team==Team.WHITE?1:-1))
				return false;
			
			//Not a capture (Already not own piece)
			if(Game.getSquare(rank, file)==null) {
				//Unless En Passant
				//if(Game.getSquare(rank - tm, file)!=null && Game.getSquare(rank - tm, file))
				//	return true;
				return false;
			}
			return true;
		}
		
		//Moved more than 2 forward
		if(Math.abs(rank - super.rank)>2)
			return false;
		
		//Moved 2 forward on not 1st turn
		if(Math.abs(rank - super.rank)==2 && super.lastMoved!=0)
			return false;
		
		//Needs empty square
		if(Game.getSquare(rank, file)!=null)
			return false;

		return true;*/
	}

	public String toString(){
		return super.toString() + 'P';
	}
}
