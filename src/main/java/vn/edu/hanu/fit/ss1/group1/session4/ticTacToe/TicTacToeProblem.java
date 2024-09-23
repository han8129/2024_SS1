package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

public class TicTacToeProblem extends AbstractProblem {
    final static int ourPlayer = Game.xCell;
    final static int otherPlayer = Game.oCell;
    final static int draw = Game.emptyCell;

    @Override
    public void evaluate(Solution solution) {
        Game game = Game.getGame(solution.getVariable(0));
        int gameResult = switch (game.getWinningPlayer()) {
            case ourPlayer -> 0;
            case draw -> 1;
            case otherPlayer -> 3;
            default -> 99;
        };

        // the lower the value the better fit the solution is, only work with NSGA
        solution.setObjective(0, gameResult);
        solution.setObjective(1, game.getMoves().size());
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 2);
        Game game = new Game(6);

        game.run();

        solution.setVariable(0, game);
        return solution;
    }

    public TicTacToeProblem() {
        super(1, 2, 0);
    }
}
