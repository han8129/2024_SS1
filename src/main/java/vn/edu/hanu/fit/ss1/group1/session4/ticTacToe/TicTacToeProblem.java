package TicTacToe3;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class TicTacToeProblem extends AbstractProblem implements Problem{

    public TicTacToeProblem() {
        // Số biến quyết định là 1 (bảng 5x5), số mục tiêu là 2
        super(1, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        TicTacToeVariable variable = (TicTacToeVariable) solution.getVariable(0);
        Boolean[] board = variable.getBoard();

        // Evaluate the result based on the current state of the board
        int result = checkGameResult(board);

        // If there's a winner or a draw, set the objectives and end the game
        if (result != -1) {
            solution.setObjective(0, result); // Set win or draw condition
            solution.setObjective(1, countMoves(board)); // Number of moves made
        } else {
            solution.setObjective(0, Double.MAX_VALUE); // Game still ongoing
            solution.setObjective(1, Double.MAX_VALUE); // Game still ongoing
        }
    }


    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 2);
        solution.setVariable(0, new TicTacToeVariable());
        ((TicTacToeVariable) solution.getVariable(0)).randomize(); // Gán nước đi ngẫu nhiên
        return solution;
    }

    // Hàm kiểm tra kết quả trận đấu
    private int checkGameResult(Boolean[] board) {
        // Convert board to 2D for easier manipulation
        Boolean[][] grid = new Boolean[5][5];
        for (int i = 0; i < board.length; i++) {
            grid[i / 5][i % 5] = board[i];
        }

        // Check rows, columns, and diagonals for winning conditions
        if (checkWin(grid, true)) return 2; // Player X wins
        if (checkWin(grid, false)) return 0; // Player O wins

        // If no win condition and the board is full, return 1 for a draw
        if (isBoardFull(board)) return 1;

        // If no winner and the board is not full, return a special value (e.g., -1) to indicate the game continues
        return -1;
    }

    // Check if all cells on the board are filled (used to detect draw condition)
    private boolean isBoardFull(Boolean[] board) {
        for (Boolean cell : board) {
            if (cell == null) {
                return false; // There are still empty cells
            }
        }
        return true; // All cells are filled
    }


    private boolean checkWin(Boolean[][] grid, boolean player) {
        // Check rows, columns, and diagonals for 3 consecutive cells
        for (int i = 0; i < 5; i++) {
            if (checkRow(grid, i, player) || checkColumn(grid, i, player)) {
                return true; // A player wins if any row or column has 3 consecutive symbols
            }
        }

        // Check diagonals for 3 consecutive cells
        return checkDiagonal(grid, player);
    }

    private boolean checkRow(Boolean[][] grid, int row, boolean player) {
        // Check for 3 consecutive cells in a row
        for (int i = 0; i <= 2; i++) {  // Stop at the third cell to avoid index out of bounds
            if (grid[row][i] != null && grid[row][i] == player &&
                    grid[row][i + 1] != null && grid[row][i + 1] == player &&
                    grid[row][i + 2] != null && grid[row][i + 2] == player) {
                return true; // 3 consecutive cells found
            }
        }
        return false;
    }

    private boolean checkColumn(Boolean[][] grid, int col, boolean player) {
        // Check for 3 consecutive cells in a column
        for (int i = 0; i <= 2; i++) {  // Stop at the third row to avoid overflow
            if (grid[i][col] != null && grid[i][col] == player &&
                    grid[i + 1][col] != null && grid[i + 1][col] == player &&
                    grid[i + 2][col] != null && grid[i + 2][col] == player) {
                return true; // 3 consecutive cells found
            }
        }
        return false;
    }

    private boolean checkDiagonal(Boolean[][] grid, boolean player) {
        // Check for 3 consecutive cells in the main diagonal
        for (int i = 0; i <= 2; i++) {
            if (grid[i][i] != null && grid[i][i] == player &&
                    grid[i + 1][i + 1] != null && grid[i + 1][i + 1] == player &&
                    grid[i + 2][i + 2] != null && grid[i + 2][i + 2] == player) {
                return true; // 3 consecutive cells found in main diagonal
            }
        }

        // Check for 3 consecutive cells in the anti-diagonal
        for (int i = 0; i <= 2; i++) {
            if (grid[i][4 - i] != null && grid[i][4 - i] == player &&
                    grid[i + 1][3 - i] != null && grid[i + 1][3 - i] == player &&
                    grid[i + 2][2 - i] != null && grid[i + 2][2 - i] == player) {
                return true; // 3 consecutive cells found in anti-diagonal
            }
        }

        return false;
    }


    // Hàm đếm số nước đi trong trận đấu
    private int countMoves(Boolean[] board) {
        int moves = 0;
        for (Boolean cell : board) {
            if (cell != null) {
                moves++;
            }
        }
        return moves;
    }
}

