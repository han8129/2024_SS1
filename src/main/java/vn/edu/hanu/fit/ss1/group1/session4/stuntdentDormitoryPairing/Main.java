package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Observations;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    List<Student> students;
    List<Dormitory> dormitories;

    // Constructor to initialize students and dormitories
    public Main(List<Student> students, List<Dormitory> dormitories) {
        this.students = students;
        this.dormitories = dormitories;
    }

    public void runOptimization() {
        StudentDormitoryProblem problem = new StudentDormitoryProblem(students, dormitories);
        NondominatedPopulation referenceSet = new NondominatedPopulation();
        Solution solution1 = new Solution(students.size(), 3);  // 3 objectives
        for (int i = 0; i < students.size(); i++) solution1.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution1.setObjective(0, 10);
        solution1.setObjective(1, 2.4);
        solution1.setObjective(2, 1);
        Solution solution2 = new Solution(students.size(), 3);  // 3 objectives
        for (int i = 0; i < students.size(); i++) solution2.setVariable(i, EncodingUtils.newInt(0, dormitories.size() - 1));
        solution2.setObjective(0, 2);
        solution2.setObjective(1, 1);
        solution2.setObjective(2, 4);
        referenceSet.add(solution1);
        referenceSet.add(solution2);
        String[] algorithms = { "NSGAII", "GDE3", "eMOEA" };

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
                .includeHypervolume()
                .includeGenerationalDistance();
        for (String algorithm : algorithms) analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).runSeeds(50));
        analyzer.display();
        Observations observations = instrumenter.getObservations();
        observations.display();
        new Plot()
                .add(observations)
                .show();
    }

    public static void main(String[] args) {
        List<Student> students = initializeStudents();
        List<Dormitory> dormitories = initializeDormitories();

        Main optimizationApp = new Main(students, dormitories);
        optimizationApp.runOptimization();
    }

    private static List<Student> initializeStudents() {
        Student student1 = new Student(
                "Alice",
                new ArrayList<>(),
                "Introverted",
                new ArrayList<>(Arrays.asList("Quiet", "Studious"))
        );

        Student student2 = new Student(
                "Bob",
                new ArrayList<>(),
                "Extroverted",
                new ArrayList<>(Arrays.asList("Social", "Friendly"))
        );

        Student student3 = new Student(
                "Charlie",
                new ArrayList<>(),
                "Studious",
                new ArrayList<>(Arrays.asList("Quiet", "Studious"))
        );

        Student student4 = new Student(
                "David",
                new ArrayList<>(),
                "Extroverted",
                new ArrayList<>(Arrays.asList("Social", "Friendly"))
        );

        return new ArrayList<>(Arrays.asList(student1, student2, student3, student4));
    }

    private static List<Dormitory> initializeDormitories() {
        Dormitory dormitory1 = new Dormitory(
                "Dormitory A",
                new ArrayList<>(),
                10,
                "Quiet",
                new ArrayList<>(Arrays.asList("Studious", "Introverted"))
        );

        Dormitory dormitory2 = new Dormitory(
                "Dormitory B",
                new ArrayList<>(),
                10,
                "Social",
                new ArrayList<>(Arrays.asList("Extroverted", "Friendly"))
        );

        // Set preferences for dormitories
        dormitory1.setPreferences(List.of(initializeStudents().get(0), initializeStudents().get(1))); // Alice and Bob
        dormitory2.setPreferences(List.of(initializeStudents().get(2))); // Charlie

        return new ArrayList<>(Arrays.asList(dormitory1, dormitory2));
    }
}