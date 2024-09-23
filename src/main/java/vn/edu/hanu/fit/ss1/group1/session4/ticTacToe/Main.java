package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

public class Main {
    public static void main(String[] args) {
        Problem problem = new TicTacToeProblem();
        NSGAII algorithm = new NSGAII(problem);
        // custom variable need custom variation for crossover/selection algorithm
        algorithm.setVariation(new GameVariation(10, 3));
        algorithm.run(200); // should keep this small, can cause heep out of mem

        for (Solution solution : algorithm.getResult()) {
            System.out.println(solution.getVariable(0));
        }

        algorithm.getResult().display();
    }
}
