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

        // Đánh giá kết quả trận đấu: thắng, hòa, thua
        int result = checkGameResult(board);

        // Đánh giá chiều dài trận đấu (số nước đi)
        int moves = countMoves(board);

        // Đặt các mục tiêu cho NSGA-III
        solution.setObjective(0, result); // Mục tiêu thắng
        solution.setObjective(1, moves);  // Độ dài trận đấu
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
        return 1; // Assume draw for simplicity
    }

    private boolean checkWin(Boolean[][] grid, boolean player) {
        // Check rows and columns
        for (int i = 0; i < 5; i++) {
            if ((checkRow(grid, i, player)) || (checkColumn(grid, i, player))) return true;
        }
        // Check diagonals
        return checkDiagonal(grid, player);
    }

    private boolean checkRow(Boolean[][] grid, int row, boolean player) {
        for (int i = 0; i < 5; i++) {
            if (grid[row][i] == null || grid[row][i] != player) return false;
        }
        return true;
    }

    private boolean checkColumn(Boolean[][] grid, int col, boolean player) {
        for (int i = 0; i < 5; i++) {
            if (grid[i][col] == null || grid[i][col] != player) return false;
        }
        return true;
    }

    private boolean checkDiagonal(Boolean[][] grid, boolean player) {
        // Check main diagonal
        boolean win = true;
        for (int i = 0; i < 5; i++) {
            if (grid[i][i] == null || grid[i][i] != player) win = false;
        }
        if (win) return true;

        // Check anti-diagonal
        win = true;
        for (int i = 0; i < 5; i++) {
            if (grid[i][4 - i] == null || grid[i][4 - i] != player) return false;
        }
        return win;
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

