package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing.n_sets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // Example usage
        Set<Entity> students = IntStream.range(0, 50)
                .mapToObj(i -> new Entity("Student" + i, "Student", 1))
                .collect(Collectors.toSet());

        Set<Entity> dormitories = IntStream.range(0, 5)
                .mapToObj(i -> new Entity("Dormitory" + i, "Dormitory", 10 + i))
                .collect(Collectors.toSet());

        Set<Entity> clubs = IntStream.range(0, 10)
                .mapToObj(i -> new Entity("Club" + i, "Club", 5 + i % 3))
                .collect(Collectors.toSet());

        Random random = new Random();
        for (Entity student : students) {
            student.setPreferences(new ArrayList<>(dormitories));
            Collections.shuffle(student.getPreferences());
            student.getPreferences().addAll(new ArrayList<>(clubs));
            Collections.shuffle(student.getPreferences());
        }

        for (Entity dormitory : dormitories) {
            dormitory.setPreferences(new ArrayList<>(students));
            Collections.shuffle(dormitory.getPreferences());
        }

        for (Entity club : clubs) {
            club.setPreferences(new ArrayList<>(students));
            Collections.shuffle(club.getPreferences());
        }

        List<Set<Entity>> entitySets = Arrays.asList(students, dormitories, clubs);
        NSetMatchingProblem problem = new NSetMatchingProblem(entitySets);

        // Add some excluded matches
        problem.addExcludedMatch(Arrays.asList(
                students.iterator().next(),
                dormitories.iterator().next(),
                clubs.iterator().next()
        ));

        System.out.println("Problem initialized with " + students.size() + " students, " +
                dormitories.size() + " dormitories, and " + clubs.size() + " clubs.");
    }
}
