package game.pieces;

import game.Game;
import game.util.Team;

import java.util.ArrayList;

public class Knight extends Piece {
	
	public Knight(Team t, int rank, int file) {
		super(t, rank, file);
	}
	public Knight(int rank, int file) {
		super(Team.WHITE, rank, file);
	}
	
	/*
	 * Checks if a piece could move to a square ignoring checks
	 */
	public ArrayList<int[]> getPossibleMoves() {

		ArrayList<int[]> out = new ArrayList<>();

		for (int i = 0; i < 8; i++) {
			int[] checking = new int[]{super.rank+Game.KnightR[i], super.file+Game.KnightF[i]};
			if(Game.inBounds(checking)) {
				Piece p = Game.getSquare(checking);
				if(p==null || p.getTeam()!=super.team)
					out.add(checking);
			}
		}

		return out;

		/*//Can't capture own pieces
		if(Game.getSquare(rank, file) != null && Game.getSquare(rank, file).getTeam() == super.team)
			return false;
		
		int dR = Math.abs(rank - super.rank);
		int dF = Math.abs(file - super.file);
		
		return (dR==2&&dF==1)||(dF==2&&dR==1);*/
	}

	public String toString(){
		return super.toString() + 'N';
	}
}
