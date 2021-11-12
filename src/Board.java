import java.util.Comparator;

public class Board implements Comparable<Board> {
    byte[] array;
    Board parent;
    int heuristic;
    int moves;
    Board(byte[] array, Board parent, int heuristic, int moves) {
        this.array = array;
        this.parent = parent;
        this.heuristic = heuristic;
        this.moves = moves;
    }


    @Override
    public int compareTo(Board y) {
        if (this.heuristic + this.moves < y.heuristic + y.moves) {
            return -1;
        }
        if (this.heuristic + this.moves > y.heuristic + y.moves) {
            return 1;
        }
        return 0;
    }
}
