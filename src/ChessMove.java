public class ChessMove {
    String start;
    String stop;
    int value;

    boolean isLegal;

    public ChessMove(String start, String stop) {
        this.start = start;
        this.stop  = stop;
        this.isLegal = true;
    }

    public ChessMove(String move) {
//       expected as e2-e4 format
        this.start = move.substring(0,2);
        this.stop = move.substring(3,5);
        this.isLegal = true;
    }

    public ChessMove(Position start, Position stop, int value) {
        this.start = start.makeString();
        this.stop  = stop.makeString();
        this.value = value;
        this.isLegal = true;
    }

    public void print () {
        System.out.print(toString());
    }

    public String toString() {
        return this.start + "-" + this.stop + " (" + value + ")" ;
    }

}
