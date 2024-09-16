package TicTacToe3;

import org.moeaframework.core.Variable;
import java.util.Random;
import java.util.Arrays;

public class TicTacToeVariable implements Variable {
    private Boolean[] board; // 25-element array

    public TicTacToeVariable() {
        // Khởi tạo một bảng rỗng với tất cả các giá trị là null
        this.board = new Boolean[25];
    }

    public TicTacToeVariable(Boolean[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    @Override
    public Variable copy() {
        return new TicTacToeVariable(this.board);
    }

    @Override
    public void randomize() {
        Random random = new Random();
        for (int i = 0; i < board.length; i++) {
            int rand = random.nextInt(3); // Randomize between null, true (X), and false (O)
            if (rand == 0) {
                board[i] = null;
            } else if (rand == 1) {
                board[i] = true; // Player X
            } else {
                board[i] = false; // Player O
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Boolean cell : board) {
            if (cell == null) {
                sb.append("_ "); // Empty cell
            } else if (cell) {
                sb.append("X "); // Player X
            } else {
                sb.append("O "); // Player O
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String encode() {
        StringBuilder sb = new StringBuilder();
        for (Boolean cell : board) {
            if (cell == null) {
                sb.append("0"); // Null cell encoded as '0'
            } else if (cell) {
                sb.append("1"); // True (X) encoded as '1'
            } else {
                sb.append("2"); // False (O) encoded as '2'
            }
        }
        return sb.toString();
    }

    @Override
    public void decode(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '0') {
                board[i] = null;
            } else if (c == '1') {
                board[i] = true; // Player X
            } else if (c == '2') {
                board[i] = false; // Player O
            }
        }
    }

    public Boolean[] getBoard() {
        return board;
    }
}

