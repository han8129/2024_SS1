package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.core.variable.BinaryVariable;

public class TicTacToeAIProblem extends AbstractProblem {
  private int playerToStart;

    public TicTacToeAIProblem(int playerToStart) {
        super(25, 2); // 25 ô trên bàn cờ, 2 mục tiêu (1: người chơi thắng, 2: AI thắng)
        this.playerToStart = playerToStart;
    }

    @Override
    public void evaluate(Solution solution) {
        boolean[] boardState = new boolean[25];

        // Chuyển đổi các biến trong giải pháp thành boolean để xử lý bàn cờ
        for (int i = 0; i < 25; i++) {
            boardState[i] = ((BinaryVariable) solution.getVariable(i)).get(0);
        }

        // Kiểm tra kết quả trò chơi và tính số bước đi
        boolean playerWon = checkWin(boardState, 1);
        boolean aiWon = checkWin(boardState, 2);
        int moves = evaluateMoves(boardState);

        // Đặt mục tiêu dựa trên kết quả
        if (playerWon) {
            solution.setObjective(0, moves);  // Người chơi thắng với số bước ít nhất
            solution.setObjective(1, Double.MAX_VALUE);  // AI thua
        } else if (aiWon) {
            solution.setObjective(0, Double.MAX_VALUE);  // Người chơi thua
            solution.setObjective(1, moves);  // AI thắng với số bước ít nhất
        } else {
            solution.setObjective(0, Double.MAX_VALUE);  // Không ai thắng
            solution.setObjective(1, Double.MAX_VALUE);  // Hòa
        }
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(25, 2);
        for (int i = 0; i < 25; i++) {
            solution.setVariable(i, new BinaryVariable(1)); // Sử dụng biến nhị phân cho từng ô
        }
        return solution;
    }

    // Kiểm tra xem người chơi hoặc AI có thắng hay không
    private boolean checkWin(boolean[] boardState, int player) {
        // Logic kiểm tra các hàng, cột, đường chéo (phát triển thêm)
        return false;
    }

    // Tính số bước đi đã được thực hiện
    private int evaluateMoves(boolean[] boardState) {
        int moves = 0;
        for (boolean state : boardState) {
            if (state) {
                moves++;
            }
        }
        return moves;
    }
}
