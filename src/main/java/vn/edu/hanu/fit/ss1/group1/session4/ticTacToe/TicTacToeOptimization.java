package TicTacToe3;

import org.moeaframework.algorithm.NSGAIII;
import org.moeaframework.algorithm.ReferencePointNondominatedSortingPopulation;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.initialization.RandomInitialization;
import org.moeaframework.core.operator.real.SBX;
import org.moeaframework.core.operator.real.PM;
import org.moeaframework.core.selection.TournamentSelection;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.util.TypedProperties;
import org.moeaframework.util.weights.NormalBoundaryDivisions;
import org.moeaframework.core.Population;
import org.moeaframework.core.comparator.AggregateConstraintComparator;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;

public class TicTacToeOptimization {

    public static void main(String[] args) {
        // Create a problem instance
        Problem problem = new TicTacToeProblem();

        // Properly configure NSGA-III with variation operators
        TypedProperties properties = new TypedProperties();
        properties.setDouble("mutationProbability", 0.1); // Mutation rate
        properties.setDouble("crossoverProbability", 0.9); // Crossover rate

        // Set up the divisions for NSGAIII's reference points
        NormalBoundaryDivisions divisions = NormalBoundaryDivisions.forProblem(problem);

        // Create the ReferencePointNondominatedSortingPopulation for NSGA-III
        ReferencePointNondominatedSortingPopulation population = new ReferencePointNondominatedSortingPopulation(problem.getNumberOfObjectives(), divisions);

        // Initialize the population
        RandomInitialization initialization = new RandomInitialization(problem);

        // Manually set a population size (e.g., 100)
        int populationSize = 100; // Define a reasonable population size here

        // Set up the variation operator (crossover + mutation)
        Variation variation = OperatorFactory.getInstance().getVariation("crossover+mutation", properties, problem);

        // Set up the selection operator (using Tournament Selection with ParetoDominanceComparator)
        TournamentSelection selection = new TournamentSelection(2, new ChainedComparator(
                new AggregateConstraintComparator(),
                new ParetoDominanceComparator() // Use ParetoDominanceComparator instead of abstract DominanceComparator
        ));

        // Create an instance of NSGAIII with manually set population size
        NSGAIII algorithm = new NSGAIII(
                problem,
                populationSize, // Manually set population size
                population,     // The reference point population
                selection,      // Selection operator
                variation,      // Variation operator
                initialization  // Initialization method
        );

        // Run the algorithm for the given number of iterations
        for (int i = 0; i < 10000; i++) {
            algorithm.step();
        }

        // Get the result after optimization
        NondominatedPopulation result = algorithm.getResult();

        // Display the results
        for (Solution solution : result) {
            TicTacToeVariable variable = (TicTacToeVariable) solution.getVariable(0);

            // Show the Tic-Tac-Toe board configuration
            System.out.println("Final Game Board:");
            System.out.println(variable.toString());

            // Show the objectives (game result and number of moves)
            System.out.println("Objectives:");
            System.out.println("Game Result (0=O Wins, 1=Draw, 2=X Wins): " + solution.getObjective(0));
            System.out.println("Number of Moves: " + solution.getObjective(1));
            System.out.println("========================================");
        }
    }
}
