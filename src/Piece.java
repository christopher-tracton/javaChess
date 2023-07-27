import java.util.ArrayList;

public class Piece {
    String color;
    String piece;

    public Piece(String color, String piece) {
        this.color = color;
        this.piece = piece;
    }

    public Piece() {
        this.color = "EMPTY";
        this.piece = "EMPTY";
    }

    public String shortColor() {
        if (this.color == "white") {
            return "W";
        } else if (this.color == "black") {
            return "B";
        } else {
            return " ";
        }
    }

    public String shortPiece() {
        if (this.piece == "pawn") {
            return "P";
        } else if (this.piece == "rook") {
            return "R";
        } else if (this.piece == "bishop") {
            return "B";
        } else if (this.piece == "knight") {
            return "N";
        } else if (this.piece == "queen") {
            return "Q";
        } else if (this.piece == "king") {
            return "K";
        } else {
            return " ";
        }
    }

    public int value() {
        if (this.piece == "pawn") {
            return 1;
        } else if (this.piece == "rook") {
            return 5;
        } else if (this.piece == "bishop") {
            return 3;
        } else if (this.piece == "knight") {
            return 3;
        } else if (this.piece == "queen") {
            return 9;
        } else if (this.piece == "king") {
            return 1000;
        } else {
            return 0;
        }
    }

    ArrayList<Position> directions() {
        ArrayList<Position> directions = new ArrayList<Position>();

        if (this.isKnight()) {
            directions.add(new Position(1, 2));
            directions.add(new Position(1, -2));
            directions.add(new Position(2, 1));
            directions.add(new Position(2, -1));
            directions.add(new Position(-1, 2));
            directions.add(new Position(-1, -2));
            directions.add(new Position(-2, 1));
            directions.add(new Position(-2, -1));
        }
        if (this.isBishop() || this.isQueen() || this.isKing()) {
            directions.add(new Position(1,-1));
            directions.add(new Position(1,1));
            directions.add(new Position(-1,-1));
            directions.add(new Position(-1,1));
        }
        if (this.isRook() || this.isQueen() || this.isKing()) {
            directions.add(new Position(0,-1));
            directions.add(new Position(0,1));
            directions.add(new Position(1,0));
            directions.add(new Position(-1,0));
        }

        return directions;
    }

    int maxSteps() {

        if (this.isKnight() || this.isKing()) {
            return 2;
        }
        if (this.isBishop() || this.isRook() || this.isQueen()) {
            return 8;
        }

        return 0;
    }


    public boolean isEmpty() {
        if (this.color == "EMPTY" || this.piece == "EMPTY") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPawn() {
        if (this.piece == "pawn") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRook() {
        if (this.piece == "rook") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKnight() {
        if (this.piece == "knight") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBishop() {
        if (this.piece == "bishop") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKing() {
        if (this.piece == "king") {
            return true;
        } else {
            return false;
        }
    }

    public boolean isQueen() {
        if (this.piece == "queen") {
            return true;
        } else {
            return false;
        }
    }

    public String abbreviation() {
        return (shortColor() + shortPiece());
    }

}
