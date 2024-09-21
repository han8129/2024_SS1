package TicTacToe3;

import org.moeaframework.core.Variable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeVariable implements Variable {

    private List<Boolean[]> gameStates;  // Stores the game states history
    private Boolean[] currentBoard;      // Stores the current 5x5 board

    public TicTacToeVariable() {
        this.gameStates = new ArrayList<>();
        this.currentBoard = new Boolean[25];  // Initialize 5x5 empty board
        this.gameStates.add(copyBoard(this.currentBoard));  // Save initial empty board state
    }

    @Override
    public Variable copy() {
        TicTacToeVariable copy = new TicTacToeVariable();
        copy.gameStates = new ArrayList<>(this.gameStates);  // Copy game states
        return copy;
    }

    @Override
    public void randomize() {
        Random random = new Random();
        for (int i = 0; i < currentBoard.length; i++) {
            int rand = random.nextInt(3);  // Randomly choose between X, O, or empty
            if (rand == 0) {
                currentBoard[i] = null;  // Empty cell
            } else if (rand == 1) {
                currentBoard[i] = true;   // Player X
            } else {
                currentBoard[i] = false;  // Player O
            }
        }
        this.gameStates.add(copyBoard(this.currentBoard)); // Save state after each randomization
    }

    public void randomMove() {
        Random random = new Random();
        List<Integer> availableMoves = new ArrayList<>();

        // Find all available (empty) positions
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == null) {
                availableMoves.add(i);
            }
        }

        // If there are available moves, choose one randomly and make the move
        if (!availableMoves.isEmpty()) {
            int move = availableMoves.get(random.nextInt(availableMoves.size()));

            // Alternate between Player X (true) and Player O (false)
            boolean player = (gameStates.size() % 2 == 0); // true for X, false for O
            currentBoard[move] = player; // Set the chosen position to the current player
            this.gameStates.add(copyBoard(currentBoard)); // Save the new board state
        }
    }


    public Boolean[] getBoard() {
        return currentBoard;  // Return the current board
    }

    @Override
    public String encode() {
        StringBuilder encodedString = new StringBuilder();
        for (Boolean cell : currentBoard) {
            if (cell == null) {
                encodedString.append("0");  // Empty cell encoded as '0'
            } else if (cell) {
                encodedString.append("1");  // Player X encoded as '1'
            } else {
                encodedString.append("2");  // Player O encoded as '2'
            }
        }
        return encodedString.toString();  // Return the encoded board as a string
    }

    @Override
    public void decode(String encodedValue) {
        for (int i = 0; i < encodedValue.length(); i++) {
            char c = encodedValue.charAt(i);
            if (c == '0') {
                currentBoard[i] = null;      // Empty cell
            } else if (c == '1') {
                currentBoard[i] = true;      // Player X
            } else if (c == '2') {
                currentBoard[i] = false;     // Player O
            }
        }
        this.gameStates.add(copyBoard(currentBoard));  // Save decoded state
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == null) {
                sb.append("_ ");  // Empty cell
            } else if (currentBoard[i]) {
                sb.append("X ");  // Player X
            } else {
                sb.append("O ");  // Player O
            }
            if ((i + 1) % 5 == 0) {
                sb.append("\n");  // New line after every 5 cells (for 5x5 grid format)
            }
        }
        return sb.toString().trim();  // Return the formatted board
    }

    public List<Boolean[]> getGameStates() {
        return gameStates;
    }

    private Boolean[] copyBoard(Boolean[] board) {
        Boolean[] copy = new Boolean[25];
        System.arraycopy(board, 0, copy, 0, 25);  // Copy all 25 cells of the 5x5 grid
        return copy;
    }
}
