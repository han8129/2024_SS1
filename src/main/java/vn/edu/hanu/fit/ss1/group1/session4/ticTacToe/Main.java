package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerToStart = 1; // Người chơi đi trước

        // Khởi chạy thuật toán NSGA-II để AI tối ưu hóa nước đi
        NondominatedPopulation result = new Executor()
                .withProblemClass(TicTacToeAIProblem.class, playerToStart)
                .withAlgorithm("NSGAII") // Sử dụng thuật toán NSGA-II
                .withMaxEvaluations(10000) // Tối đa 10000 lần đánh giá
                .run();

        // Khởi tạo bàn cờ
        boolean[] boardState = new boolean[25];

        // In kết quả ban đầu từ AI
        System.out.println("AI đang phân tích:");
        printBoard(result.get(0));  // In bàn cờ từ giải pháp tốt nhất hiện tại

        // Người chơi nhập nước đi
        System.out.println("Nhập vị trí bạn muốn đi (0-24): ");
        int playerMove = scanner.nextInt();
        boardState[playerMove] = true; // Đánh dấu vị trí của người chơi

        // AI thực hiện nước đi
        System.out.println("AI đang thực hiện nước đi...");
        Solution bestMove = findBestMove(result); // AI tìm nước đi tốt nhất
        applyBestMove(boardState, bestMove); // Cập nhật bàn cờ với nước đi của AI

        // In trạng thái bàn cờ sau khi AI thực hiện nước đi
        printBoard(bestMove);

        // Kiểm tra kết thúc trò chơi
        if (checkGameOver(boardState)) {
            System.out.println("Trò chơi kết thúc!");
        }
    }

    // Hàm kiểm tra trạng thái kết thúc của trò chơi
    private static boolean checkGameOver(boolean[] boardState) {
        // Logic kiểm tra thắng thua hoặc hòa (phát triển thêm)
        return false;
    }

    // Tìm giải pháp tối ưu của AI
    private static Solution findBestMove(NondominatedPopulation result) {
        return result.get(0); // Chọn giải pháp tốt nhất từ tập hợp không bị chi phối
    }

    // Áp dụng nước đi của AI lên bàn cờ
    private static void applyBestMove(boolean[] boardState, Solution bestMove) {
        for (int i = 0; i < 25; i++) {
            boardState[i] = ((BinaryVariable) bestMove.getVariable(i)).get(0); // Cập nhật trạng thái bàn cờ
        }
    }

    // Hàm in trạng thái bàn cờ
    private static void printBoard(Solution solution) {
        for (int i = 0; i < 25; i++) {
            if (i % 5 == 0) {
                System.out.println();  // Xuống dòng sau mỗi hàng
            }
            System.out.print(((BinaryVariable) solution.getVariable(i)).get(0) ? " X " : " O ");
        }
        System.out.println();
    }
}
