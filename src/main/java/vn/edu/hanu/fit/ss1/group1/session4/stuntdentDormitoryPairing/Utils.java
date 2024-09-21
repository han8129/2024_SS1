package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import org.moeaframework.problem.misc.Lis;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    private static final int NUM_STUDENTS = 100;
    private static final int NUM_DORMITORIES = 10;
    private static final int MIN_DORM_CAPACITY = 8;
    private static final int MAX_DORM_CAPACITY = 15;

    private static final String[] PERSONALITY_TRAITS = {
            "Introverted", "Extroverted", "Studious", "Athletic", "Artistic", "Tech-savvy",
            "Environmentalist", "Entrepreneurial", "Musical", "Leadership"
    };

    private static final String[] STUDENT_PREFERENCES = {
            "Quiet", "Social", "Studious", "Fitness", "Creative", "Tech-friendly",
            "Eco-friendly", "Business-oriented", "Music-friendly", "Community-focused"
    };

    private static final String[] DORM_TRAITS = {
            "Quiet", "Social", "Academic", "Athletic", "Artistic", "Tech-hub",
            "Eco-friendly", "Entrepreneurial", "Musical", "Leadership-focused"
    };

    public static List<Student> initializeStudents() {
        Random random = new Random();
        return IntStream.range(0, NUM_STUDENTS)
                .mapToObj(i -> {
                    String name = "Student" + (i + 1);
                    String personalityTrait = PERSONALITY_TRAITS[random.nextInt(PERSONALITY_TRAITS.length)];
                    List<String> preferences = getRandomSublist(STUDENT_PREFERENCES, 2 + random.nextInt(3));
                    return new Student(name, new ArrayList<>(), personalityTrait, preferences);
                })
                .collect(Collectors.toList());
    }

    public static List<Dormitory> initializeDormitories() {
        Random random = new Random();
        List<Student> allStudents = initializeStudents();

        List<Dormitory> dormitories = IntStream.range(0, NUM_DORMITORIES)
                .mapToObj(i -> {
                    String name = "Dormitory" + (char)('A' + i);
                    int capacity = MIN_DORM_CAPACITY + random.nextInt(MAX_DORM_CAPACITY - MIN_DORM_CAPACITY + 1);
                    String dormTrait = DORM_TRAITS[random.nextInt(DORM_TRAITS.length)];
                    List<String> preferences = getRandomSublist(STUDENT_PREFERENCES, 2 + random.nextInt(3));
                    return new Dormitory(name, new ArrayList<>(), capacity, dormTrait, preferences);
                })
                .collect(Collectors.toList());

        // Set preferences for students
        for (Student student : allStudents) {
            List<Dormitory> preferences = new ArrayList<>(dormitories);
            Collections.shuffle(preferences);
            student.setPreferences(preferences);
        }

        // Set preferences for dormitories
        for (Dormitory dormitory : dormitories) {
            List<Student> preferences = new ArrayList<>(allStudents);
            Collections.shuffle(preferences);
            dormitory.setPreferences(preferences.subList(0, dormitory.getCapacity() * 2)); // Each dorm prefers twice its capacity
        }

        return dormitories;
    }

    private static <T> List<T> getRandomSublist(T[] array, int size) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        return list.subList(0, Math.min(size, list.size()));
    }

    public static void initializeExcludedPairs(StudentDormitoryProblem problem, List<Student> students, List<Dormitory> dormitories) {
        // Example: Exclude Alice from Dormitory B and Bob from Dormitory A
        Student alice = findStudentByName("Student 1", students);
        Student bob = findStudentByName("Student 2",students);
        Dormitory dormA = findDormitoryByName("Dormitory A",dormitories);
        Dormitory dormB = findDormitoryByName("Dormitory B", dormitories);

        if (alice != null && dormB != null) {
            problem.addExcludedPair(alice, dormB);
            System.out.println("Excluded pair added: Alice and Dormitory B");
        }

        if (bob != null && dormA != null) {
            problem.addExcludedPair(bob, dormA);
            System.out.println("Excluded pair added: Bob and Dormitory A");
        }

        // You can add more excluded pairs here as needed
    }

    private static Student findStudentByName(String name, List<Student> students) {
        return students.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private static Dormitory findDormitoryByName(String name, List<Dormitory> dormitories) {
        return dormitories.stream()
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
