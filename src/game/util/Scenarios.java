package game.util;

import game.pieces.*;

public class Scenarios {

    public static Piece[][] getNormal(){
        return new Piece[][]
                {{new Rook(0,0), new Knight(0,1), new Bishop(0,2), new Queen(0,3), new King(0,4), new Bishop(0,5), new Knight(0,6), new Rook(0,7)},
                        {new Pawn(1,0),new Pawn(1,1),new Pawn(1,2),new Pawn(1,3),new Pawn(1,4),new Pawn(1,5),new Pawn(1,6),new Pawn(1,7)},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {new Pawn(Team.BLACK, 6, 0),new Pawn(Team.BLACK, 6, 1),new Pawn(Team.BLACK, 6, 2),new Pawn(Team.BLACK, 6, 3),new Pawn(Team.BLACK, 6, 4),new Pawn(Team.BLACK, 6, 5),new Pawn(Team.BLACK, 6, 6),new Pawn(Team.BLACK, 6, 7)},
                        {new Rook(Team.BLACK, 7, 0), new Knight(Team.BLACK, 7, 1), new Bishop(Team.BLACK, 7, 2), new Queen(Team.BLACK, 7, 3), new King(Team.BLACK, 7, 4), new Bishop(Team.BLACK, 7, 5), new Knight(Team.BLACK, 7, 6), new Rook(Team.BLACK, 7, 7)}};
    }

    public static Piece[][] getEndgame(){
        return new Piece[][]
                        {{null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,null,null,null,null,null},
                        {null,null,null,new Queen(5, 3),null,null,null,null},
                        {new King(Team.BLACK, 6, 0),null,new Pawn(6, 2),new King(6, 3),null,null,null,null},
                        {null,null,null,null,null,null,null,null}};
    }

}
