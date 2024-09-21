package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Observations;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.*;

import static vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing.Utils.*;

public class Main {
    List<Student> students;
    List<Dormitory> dormitories;

    public Main() {
        this.students = initializeStudents();
        this.dormitories = initializeDormitories();
    }

    public static void main(String[] args) {
        Main optimizationApp = new Main();
        optimizationApp.runOptimization();
    }

    public void runOptimization() {
        StudentDormitoryProblem problem = new StudentDormitoryProblem(students, dormitories);
        initializeExcludedPairs(problem, students, dormitories);
        NondominatedPopulation referenceSet = new NondominatedPopulation();
        Solution solution1 = new Solution(students.size(), 3);  // 3 objectives
        for (int i = 0; i < students.size(); i++)
            solution1.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution1.setObjective(0, 20);
        solution1.setObjective(1, 5);
        solution1.setObjective(2, 2);
        Solution solution2 = new Solution(students.size(), 3);  // 3 objectives
        for (int i = 0; i < students.size(); i++)
            solution2.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution2.setObjective(0, 10);
        solution2.setObjective(1, 10);
        solution2.setObjective(2, 4);
        referenceSet.add(solution1);
        referenceSet.add(solution2);
        String[] algorithms = {"NSGAII", "GDE3", "eMOEA"};

        Instrumenter instrumenter = new Instrumenter()
                .withProblem(problem)
                .withFrequency(100)
                .withReferenceSet(referenceSet)
                .attachAllMetricCollectors();
        Executor executor = new Executor()
                .withProblem(problem)
                .withProperty("populationSize", 10)
                .withInstrumenter(instrumenter)
                .withMaxEvaluations(1000);
        Analyzer analyzer = new Analyzer()
                .withSameProblemAs(executor)
                .withReferenceSet(referenceSet)
                .includeGenerationalDistance();
        for (String algorithm : algorithms) analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).runSeeds(50));
        analyzer.display();
        Observations observations = instrumenter.getObservations();
        observations.display();
        new Plot()
                .add(observations)
                .show();
    }
}