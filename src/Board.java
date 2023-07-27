import java.util.ArrayList;
import java.util.Random;

public class Board {

    History history;
    Piece board[][];
    boolean whiteToPlay = true;
    int layer = 1;
    int maxLayers;

    public Board(int maxLayers) {
        board = new Piece[8][8];
        initialSetup();
        this.maxLayers = maxLayers;
        history = new History();
    }

    public Board(Board boardToCopy) {
        board = new Piece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = boardToCopy.board[row][col];
            }
        }

        whiteToPlay = boardToCopy.whiteToPlay;
        layer = boardToCopy.layer + 1;
        history = new History(boardToCopy.history);
        maxLayers = boardToCopy.maxLayers;
    }


    public void takeIfOpposite(ArrayList<ChessMove> retval, int row1, int col1, int row2, int col2) {
        int moveValue;
        Piece target = board[row2][col2];
        Piece source = board[row1][col1];

        if (!target.isEmpty() && (target.color != source.color)) {
            Position startPos = new Position(row1,col1);
            Position stopPos  = new Position(row2,col2);
            ChessMove move = assignMoveValue(startPos, stopPos);
            if (move.isLegal) {
                retval.add(move);
            }
        }
    }

    public ChessMove assignMoveValue(Position startPos, Position stopPos) {
        int moveValue = this.value(stopPos);
        ChessMove move = new ChessMove(startPos, stopPos, moveValue);

        int BRV = 0;

        if ((this.layer == 2) && (moveValue == 1000)) {
            BRV = 0;  // just consider the move.value which has already been assigned to move
//            System.out.print("\n" + move + "don't continue to evaluate - it doesn't matter - prior move is illegal\n");
        }
        else {
            ChessMove bestResponse = this.bestResponse(move);
            if (bestResponse != null) {
                BRV =bestResponse.value;
            }
            else {
                BRV = 0;
            }
        }

        if ((this.layer == 1) && (BRV == 1000)) {
            move.isLegal = false;
            System.out.print(" " + move + " is illegal");
        }
        else {
            move.value -= BRV;
        }

        return move;
    }

    public void moveIfEmpty(ArrayList<ChessMove> retval, int row1, int col1, int row2, int col2) {
        int moveValue;
        Piece target = board[row2][col2];


        if (target.isEmpty()) {
            Position startPos = new Position(row1,col1);
            Position stopPos = new Position(row2, col2);
            ChessMove move = assignMoveValue(startPos, stopPos);
            if (move.isLegal) {
                retval.add(move);
            }
        }
    }

    boolean isOpposite(Position position) {
        if (whiteToPlay)
            return board[position.r][position.c].color.equals("black");
        else
            return board[position.r][position.c].color.equals("white");
    }

    int value(Position position) {
        if (isOpposite(position))
            return board[position.r][position.c].value();
        else
            return 0;
    }


    ArrayList<ChessMove> pawnMoves(int row, int col, String color) {

        ArrayList<ChessMove> retval = new ArrayList();

        // white pawns in 1 can move 2 if empty
        // black pawns in 6 can move 2 if empty
        // otherwise can move 1 if empty
        // can take diagnoally 1 if opposite color
        if (color.equals("black")) {
            if (row > 0) {
                this.moveIfEmpty(retval,row,col,row-1,col);

                if (col > 0) {
                    this.takeIfOpposite(retval,row,col,row-1,col-1);
                }

                if (col < 7) {
                    this.takeIfOpposite(retval,row,col,row-1,col+1);
                }
            }
            if (row == 6) {
                Piece target = board[row-1][col];
                if (target.isEmpty()) {
                    this.moveIfEmpty(retval,row,col,row-2,col);
                }
            }
        } else if (color.equals("white")) {
            if (row < 7) {
                this.moveIfEmpty(retval,row,col,row+1,col);

                if (col > 0) {
                    this.takeIfOpposite(retval,row,col,row+1,col-1);
                }

                if (col < 7) {
                    this.takeIfOpposite(retval,row,col,row+1,col+1);
                }
            }
            if (row == 1) {
                Piece target = board[row+1][col];
                if (target.isEmpty()) {
                    this.moveIfEmpty(retval,row,col,row+2,col);
                }
            }
        }

        return retval;
    }


    ArrayList<ChessMove> pieceMoves(int row, int col) {

//        System.out.println("\n in bishopMoves row " + row  + " col " + col);
        int moveValue = 0;

        ArrayList<ChessMove> moves = new ArrayList();
        ArrayList<Position> targets = new ArrayList();

        Piece piece = board[row][col];
        ArrayList<Position> directions = piece.directions();
        int maxSteps = piece.maxSteps();

        for (Position direction: directions) {
            for (int steps = 1; steps < maxSteps; steps++) {
                Position offset = direction.mutli(steps);
                offset = offset.add(row, col);

                if (!offset.onBoard()) {
                    break;
                }

                Piece pieceAtOffset = board[offset.r][offset.c];

                if (!pieceAtOffset.isEmpty() && !this.isOpposite(offset)) {
                    break;
                }

                targets.add(offset);

                if (!pieceAtOffset.isEmpty()) {
                    break;
                }
            }
        }

        for (Position target : targets) {
            Position start = new Position(row,col);
            ChessMove move = assignMoveValue(start, target);
            if (move.isLegal) {
                moves.add(move);
            }
        }

//        System.out.print("\nPiece Moves : " + moves + "\n");
        return moves;
    }

    public ChessMove bestMove(ArrayList<ChessMove> moves) {
        int max = Integer.MIN_VALUE;
        ArrayList<ChessMove> chessMoves = new ArrayList<ChessMove>();

        for (ChessMove move : moves) {
            if (move.value > max) {
                max = move.value;
                chessMoves.clear();
                chessMoves.add(move);
            }
            if (move.value == max) {
                chessMoves.add(move);
            }
        }

        Random rand = new Random();
        return chessMoves.get(rand.nextInt(chessMoves.size()));
    }

    public ChessMove bestResponse(ChessMove possibleMove) {
//        System.out.print("\nlayer " + layer + " considering " + possibleMove + " ");

        ChessMove bestResponse = null;

        if (this.layer < this.maxLayers) {
            Board possibleBoard = new Board(this);
            possibleBoard.move(possibleMove);
            ArrayList<ChessMove> possibleResponses = possibleBoard.legalMoves();
            bestResponse = this.bestMove(possibleResponses);
            System.out.print("\n");
            for (int i = 1; i < layer; i++)
                System.out.print("\t");
            System.out.print("layer : " + layer + " after " + (whiteToPlay?"WHITE ":"BLACK ") + possibleMove + (whiteToPlay?" BLACK ":" WHITE ") + bestResponse + " with value " + bestResponse.value + " follows");
            try {
                Thread.sleep(150);
            }
            catch(Exception e) {

            }
        }

        return bestResponse;
    }

    public ArrayList<ChessMove> legalMoves () {
        ArrayList<ChessMove> retval = new ArrayList();

        String color;

        if (whiteToPlay) {
            color = "white";
        }
        else {
            color = "black";
        }


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];

                if (piece.color.equals(color)) {
                    if (piece.isPawn()) {
                        retval.addAll(this.pawnMoves(row,col,color));
                    } else if (!piece.isEmpty()) {
                        retval.addAll(this.pieceMoves(row,col));
                    }
                }
            }
        }

        return retval;
    }

    public int convertToColumn(String position) {
        char row = position.charAt(0);

        return (int)row - (int)'a';
    }

    public int convertToRow(String position) {
        char column = position.charAt(1);

        int colInt = (int)column - (int)'0' - 1;

        return colInt;
    }

    public void move(ChessMove theMove) {

        int row1 = convertToRow(theMove.start);
        int col1 = convertToColumn(theMove.start);
        int row2 = convertToRow(theMove.stop);
        int col2 = convertToColumn(theMove.stop);

        board[row2][col2] = board[row1][col1];
        board[row1][col1] = new Piece();

        history.add(theMove);

        whiteToPlay = !whiteToPlay;
    }

    public void print() {
        System.out.println("     A    B    C    D    E    F    G    H  ");
        System.out.println("  +----+----+----+----+----+----+----+----+");
        for (int row = 7; row >= 0; row--) {
            System.out.print(row+1 + " ");
            for (int column = 0; column < 8; column++) {
                System.out.print("| " + board[row][column].abbreviation() + " ");
            }
            System.out.println("| " + (row+1));
            System.out.println("  +----+----+----+----+----+----+----+----+");
        }
        System.out.println("     A    B    C    D    E    F    G    H  ");
        System.out.println("");
    }

    public void initialSetup() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board[row][column] = new Piece();
            }
        }

        board[0][0] = new Piece("white","rook");
        board[0][1] = new Piece("white","knight");
        board[0][2] = new Piece("white","bishop");
        board[0][3] = new Piece("white","queen");
        board[0][4] = new Piece("white","king");
        board[0][5] = new Piece("white","bishop");
        board[0][6] = new Piece("white","knight");
        board[0][7] = new Piece("white","rook");
        board[7][0] = new Piece("black","rook");
        board[7][1] = new Piece("black","knight");
        board[7][2] = new Piece("black","bishop");
        board[7][3] = new Piece("black","queen");
        board[7][4] = new Piece("black","king");
        board[7][5] = new Piece("black","bishop");
        board[7][6] = new Piece("black","knight");
        board[7][7] = new Piece("black","rook");

        for (int column = 0; column < 8; column++) {
            board[1][column] = new Piece("white","pawn");
            board[6][column] = new Piece("black","pawn");
        }

        whiteToPlay = true;
    }

}
