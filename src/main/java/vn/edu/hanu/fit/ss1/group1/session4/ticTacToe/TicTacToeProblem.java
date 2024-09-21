package TicTacToe3;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class TicTacToeProblem extends AbstractProblem implements Problem {

    public TicTacToeProblem() {
        super(1, 2);  // One variable (game board), two objectives (game result and number of moves)
    }

    @Override
    public void evaluate(Solution solution) {
        TicTacToeVariable variable = (TicTacToeVariable) solution.getVariable(0);
        Boolean[] board = variable.getBoard();  // Retrieve the current board state from TicTacToeVariable

        // Evaluate the game result using the current board state
        int result = checkGameResult(board);

        // Set objectives: 0 for result (win/draw), 1 for the number of moves
        solution.setObjective(0, result);
        solution.setObjective(1, variable.getGameStates().size());
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 2);
        TicTacToeVariable game = new TicTacToeVariable();

        // Randomly play moves until the game finishes
        while (!isGameFinished(game)) {
            game.randomMove();  // Make a random move using TicTacToeVariable's method
        }

        solution.setVariable(0, game);  // Store the final game state in the solution
        return solution;
    }

    /**
     * Helper method to check if the game is finished.
     * This checks if either player has won or if the game is a draw (full board).
     *
     * @param game the current TicTacToeVariable containing the game state
     * @return true if the game is finished (win or draw), false otherwise
     */
    private boolean isGameFinished(TicTacToeVariable game) {
        Boolean[] board = game.getBoard();  // Get the current board state
        int result = checkGameResult(board);
        return (result != -1);  // Return true if there's a win or draw
    }

    /**
     * Helper method to check the result of the game.
     * @param board the current game board as a Boolean array
     * @return -1 if the game is ongoing, 0 if O wins, 1 if it's a draw, 2 if X wins
     */
    private int checkGameResult(Boolean[] board) {
        Boolean[][] grid = new Boolean[5][5];
        for (int i = 0; i < board.length; i++) {
            grid[i / 5][i % 5] = board[i];
        }

        // Check win conditions for both players
        if (checkWin(grid, true)) return 2;  // X wins
        if (checkWin(grid, false)) return 0;  // O wins
        if (isBoardFull(board)) return 1;  // Draw
        return -1;  // Game is ongoing
    }

    /**
     * Helper method to check if a player has won.
     * @param grid the 2D game grid
     * @param player true for Player X, false for Player O
     * @return true if the player has won, false otherwise
     */
    private boolean checkWin(Boolean[][] grid, boolean player) {
        // Check rows, columns, and diagonals for win condition
        for (int i = 0; i < 5; i++) {
            if (checkRow(grid, i, player) || checkColumn(grid, i, player)) {
                return true;
            }
        }
        return checkDiagonal(grid, player);
    }

    private boolean isBoardFull(Boolean[] board) {
        for (Boolean cell : board) {
            if (cell == null) {
                return false;  // There's still an empty cell
            }
        }
        return true;  // All cells are filled
    }

    private boolean checkRow(Boolean[][] grid, int row, boolean player) {
        for (int i = 0; i <= 2; i++) {  // Check for 3 consecutive cells
            if (grid[row][i] != null && grid[row][i] == player &&
                    grid[row][i + 1] != null && grid[row][i + 1] == player &&
                    grid[row][i + 2] != null && grid[row][i + 2] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumn(Boolean[][] grid, int col, boolean player) {
        for (int i = 0; i <= 2; i++) {  // Check for 3 consecutive cells
            if (grid[i][col] != null && grid[i][col] == player &&
                    grid[i + 1][col] != null && grid[i + 1][col] == player &&
                    grid[i + 2][col] != null && grid[i + 2][col] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(Boolean[][] grid, boolean player) {
        // Check main diagonal
        for (int i = 0; i <= 2; i++) {
            if (grid[i][i] != null && grid[i][i] == player &&
                    grid[i + 1][i + 1] != null && grid[i + 1][i + 1] == player &&
                    grid[i + 2][i + 2] != null && grid[i + 2][i + 2] == player) {
                return true;
            }
        }

        // Check anti-diagonal
        for (int i = 0; i <= 2; i++) {
            if (grid[i][4 - i] != null && grid[i][4 - i] == player &&
                    grid[i + 1][3 - i] != null && grid[i + 1][3 - i] == player &&
                    grid[i + 2][2 - i] != null && grid[i + 2][2 - i] == player) {
                return true;
            }
        }

        return false;
    }
}
