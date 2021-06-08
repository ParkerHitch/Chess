package game.pieces;

import game.Game;
import game.util.*;

import java.util.ArrayList;

public abstract class Piece {

	protected int lastMoved = 0;
	protected final Team team;
	protected int rank, file;

	public Piece(Team t, int rank, int file) {
		team = t;
		this.rank = rank;
		this.file = file;
	}

	public ArrayList<int[]> getPossibleMoves(){
		return null;
	}
	
	public void move(int rank, int file) {
		this.rank = rank;
		this.file = file;
		this.lastMoved = Game.getMoveNumber();
	}

	public void move(int[] square){
		move(square[0], square[1]);
	}
	
	public Team getTeam() { return team; }

	public int[] getPos() { return new int[]{rank, file}; }

	public int getLastMoved() { return lastMoved; }

	public String toString() {
		return this.team.getColor();
	}
}
