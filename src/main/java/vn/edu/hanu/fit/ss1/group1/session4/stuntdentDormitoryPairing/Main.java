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
        NondominatedPopulation referenceSet = new NondominatedPopulation();
        Solution solution1 = new Solution(students.size(), 1, 1);
        for (int i = 0; i < students.size(); i++)
            solution1.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution1.setObjective(0, 30.23);
        solution1.setConstraint(0, 0.0);
        Solution solution2 = new Solution(students.size(), 1, 1);
        for (int i = 0; i < students.size(); i++)
            solution2.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution2.setObjective(0, 10.5);
        solution2.setConstraint(0, 2.0);
        referenceSet.add(solution1);
        referenceSet.add(solution2);
        String[] algorithms = {"NSGAII", "eMOEA", "SPEA2", "IBEA"};

        Instrumenter instrumenter = new Instrumenter()
                .withProblem(problem)
                .withFrequency(100)
                .withReferenceSet(referenceSet)
                .attachAllMetricCollectors();
        Executor executor = new Executor()
                .withProblem(problem)
                .withProperty("populationSize", 100)
                .withInstrumenter(instrumenter)
                .withMaxEvaluations(1000);
        Analyzer analyzer = new Analyzer()
                .withSameProblemAs(executor)
                .withReferenceSet(referenceSet)
                .includeAllMetrics();
        for (String algorithm : algorithms) analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).runSeeds(100));
        analyzer.display();
        Observations observations = instrumenter.getObservations();
        observations.display();
        new Plot()
                .add(observations)
                .show();
    }
}