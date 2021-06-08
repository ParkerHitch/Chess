package game.pieces;

import game.Game;
import game.util.Team;

import java.util.ArrayList;
import java.util.Arrays;

public class Bishop extends Piece {

	public Bishop(Team t, int rank, int file) {
		super(t, rank, file);
	}
	public Bishop(int rank, int file) {
		super(Team.WHITE, rank, file);
	}

	/*
	 * Returns a list of possible moves for the piece ignoring check
	 */
	public ArrayList<int[]> getPossibleMoves() {

		ArrayList<int[]> out = new ArrayList<>();

		for (int r = -1; r < 2; r+=2) {
			for(int f = -1; f < 2; f+=2) {
				//Cast out ray on diagonal until it hits a piece or edge
				int i = 1;
				int[] checking = new int[]{super.rank+i*r, super.file+i*f};
				Piece checkPiece;
				while(Game.inBounds(checking)) {
					checkPiece = Game.getSquare(checking);
					if(checkPiece == null) {
						out.add(checking);
						i++;
						checking = new int[]{super.rank+i*r, super.file+i*f};
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
		if(Game.getSquare(rank, file) != null && Game.getSquare(rank, file).getTeam() == super.team)
			return false;
		
		int dR = Math.abs(rank - super.rank);
		int dF = Math.abs(file - super.file);
		
		//Not on same diagonal
		if(dR!=dF)
			return false;
		
		int dirR = (rank - super.rank) / dR;
		int dirF = (file - super.file) / dF;
		System.out.println((rank - super.rank) + "/" + dR+","+(file - super.file) +"/"+dF);
		System.out.println(dirR+","+dirF);
		for(int i=1; i<dR; i++) {
			if(Game.getSquare(super.rank+(i*dirR), super.file+(i*dirF))!=null)
				return false;
		}
		
		return true;*/
	
	}

	public String toString(){
		return super.toString() + 'B';
	}

}
