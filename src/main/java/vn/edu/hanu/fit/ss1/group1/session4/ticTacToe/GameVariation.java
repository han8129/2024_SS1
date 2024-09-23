package vn.edu.hanu.fit.ss1.group1.session4.ticTacToe;

import org.moeaframework.core.Solution;
import org.moeaframework.core.operator.real.MultiParentVariation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameVariation extends MultiParentVariation {
    /**
     * Creates a new multi-parent variation operator.
     *
     * @param numberOfParents   the number of parents required by this operator
     * @param numberOfOffspring the number of offspring produced by this operator
     */
    public GameVariation(int numberOfParents, int numberOfOffspring) {
        super(numberOfParents, numberOfOffspring);
    }

    @Override
    public String getName() {
        return "Simple Game Selection";
    }

    /**
     *
     * A simple parent selection / cross over algorithm, this is not any named
     * approach
     * @param parents the array of parent solutions
     * @return solutions that preferably won or draw
     */
    @Override
    public Solution[] evolve(Solution[] parents) {
        Solution[] parentClones = parents.clone();
        Game[] games = Arrays.stream(parentClones)
            .map(solution -> Game.getGame(solution.getVariable(0)))
            .toList()
            .toArray(new Game[0]);


        List<Game> winOrDraws = Arrays.stream(games)
            .filter(game -> vn.edu.hanu.fit.ss1.group1.session4.ticTacToe.TicTacToeProblem.ourPlayer == game.getWinningPlayer()
                | TicTacToeProblem.draw == game.getWinningPlayer())
            .toList();

        List<Game> selections = (winOrDraws.isEmpty())
            ? (List.of(games))
            : winOrDraws;

        List<Solution> children = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfOffspring; i++) {
            int index = random.nextInt(selections.size());

           Game game = (selections.get(index));
           game.randomize();
           Solution solution = new Solution(1,2);
           solution.setVariable(0, game);
           children.add(solution);
        }

        return children.toArray(new Solution[0]);
    }
}
