package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import lombok.experimental.FieldDefaults;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
import java.util.*;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StudentDormitoryProblem extends AbstractProblem {
    List<Student> students;
    List<Dormitory> dormitories;
    Set<Pair<Student, Dormitory>> excludedPairs;

    public StudentDormitoryProblem(List<Student> students, List<Dormitory> dormitories) {
        super(students.size(), 3);
        this.students = students;
        this.dormitories = dormitories;
        this.excludedPairs = new HashSet<>();
    }

    public void addExcludedPair(Student student, Dormitory dormitory) {
        excludedPairs.add(new Pair<>(student, dormitory));
    }

    @Override
    public void evaluate(Solution solution) {
        Map<Student, Dormitory> matching = runGaleShapley();
        int studentsWithoutDormitory = 0;
        int studentsInOvercrowdedDormitory = 0;
        int preferenceMismatch = 0;

        for (Dormitory dormitory : dormitories) {
            dormitory.getStudents().clear();
        }

        for (Map.Entry<Student, Dormitory> entry : matching.entrySet()) {
            Student student = entry.getKey();
            Dormitory dormitory = entry.getValue();

            if (dormitory == null) studentsWithoutDormitory++;
            else {
                if (excludedPairs.contains(new Pair<>(student, dormitory))) continue;
                if (dormitory.getStudents().size() > dormitory.getCapacity()) studentsInOvercrowdedDormitory++;
                if (!student.getPreferences().contains(dormitory) || !dormitory.getPreferences().contains(student)) preferenceMismatch++;
                if (!dormitory.getStudentPreferences().contains(student.getPersonalityTrait())) preferenceMismatch++;
                if (student.getDormPreferences().stream().noneMatch(trait -> trait.equals(dormitory.getDormTrait()))) preferenceMismatch++;
            }
        }

        solution.setObjective(0, studentsWithoutDormitory);
        solution.setObjective(1, studentsInOvercrowdedDormitory);
        solution.setObjective(2, preferenceMismatch);
    }

    private Map<Student, Dormitory> runGaleShapley() {
        Map<Student, Dormitory> matching = new HashMap<>();
        Queue<Student> freeStudents = new LinkedList<>(students);

        while (!freeStudents.isEmpty()) {
            Student student = freeStudents.poll();
            for (Dormitory dormitory : student.getPreferences()) {
                if (dormitory.getStudents().size() < dormitory.getCapacity() && !excludedPairs.contains(new Pair<>(student, dormitory))) {
                    matching.put(student, dormitory);
                    dormitory.addStudent(student);
                    break;
                } else if (dormitory.getStudents().size() == dormitory.getCapacity()) {
                    Student worstStudent = dormitory.getStudents().stream()
                            .min(Comparator.comparingInt(s -> dormitory.getPreferences().indexOf(s)))
                            .orElse(null);
                    if (worstStudent != null && dormitory.getPreferences().indexOf(student) < dormitory.getPreferences().indexOf(worstStudent)) {
                        matching.remove(worstStudent);
                        dormitory.removeStudent(worstStudent);
                        freeStudents.add(worstStudent);
                        matching.put(student, dormitory);
                        dormitory.addStudent(student);
                        break;
                    }
                }
            }
            if (!matching.containsKey(student)) matching.put(student, null); // Student couldn't be matched
        }
        return matching;
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(students.size(), 3);
        for (int i = 0; i < students.size(); i++) {
            solution.setVariable(i, EncodingUtils.newInt(-1, dormitories.size() - 1));
        }
        return solution;
    }

}