public class Position {
    int r;
    int c;

    public Position(int row, int column) {
        this.r = row;
        this.c = column;
    }

    public Position add(int row, int column) {
        return new Position(this.r + row, this.c + column);
    }

    public boolean onBoard() {
        if (this.r >= 0 && this.r < 8 && this.c >= 0 && this.c < 8)
            return true;
        else
            return false;
    }

    public String makeString() {
        char first = (char) ('a' + this.c);
        char second = (char) ('0' + this.r + 1);
        String retval = String.valueOf(first) + String.valueOf(second);
        return retval;
    }

    public Position mutli(int steps) {
        return new Position(this.r * steps, this.c * steps);
    }

    public String toString() {
        return "(" + r + "," + c + ")";
    }
}


