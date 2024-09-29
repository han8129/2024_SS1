package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomUtils;
import org.moeaframework.core.Constraint;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
import java.util.*;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StudentDormitoryProblem extends AbstractProblem {
    List<Student> students;
    List<Dormitory> dormitories;

    double alpha1 = 1.0; // Weight for minimizing preference mismatches
    double alpha2 = 1.0; // Weight for minimizing overcrowding
    double alpha3 = 1.0; // Weight for minimizing unassigned students

    public StudentDormitoryProblem(List<Student> students, List<Dormitory> dormitories) {
        super(students.size(), 1, 1);
        this.students = students;
        this.dormitories = dormitories;
    }

    @Override
    public void evaluate(Solution solution) {
        int studentsWithoutDormitory = 0;
        int studentsInOvercrowdedDormitory = 0;
        int preferenceMismatch = 0;

        for (int i = 0; i < students.size(); i++) {
            int dormitoryIndex = EncodingUtils.getInt(solution.getVariable(i));
            Student student = students.get(i);
            if (dormitoryIndex == -1) studentsWithoutDormitory++;
            else {
                Dormitory dormitory = dormitories.get(dormitoryIndex);
                dormitory.addStudent(student);
                if (dormitory.getStudents().size() > dormitory.getCapacity()) studentsInOvercrowdedDormitory++;
                if (!student.getPreferences().contains(dormitory) || !dormitory.getPreferences().contains(student)) preferenceMismatch++;
                if (!dormitory.getStudentPreferences().contains(student.getPersonalityTrait())) preferenceMismatch++;
                if (student.getDormPreferences().stream()
                        .noneMatch(trait -> trait.equals(dormitory.getDormTrait()))) {
                    preferenceMismatch++;
                }
            }
            solution.setObjective(0, alpha1 * preferenceMismatch + alpha2 * studentsInOvercrowdedDormitory + alpha3 * studentsWithoutDormitory);
            // Constraint to prevent dormitory and student with same index from being matched
            solution.setConstraint(0, Constraint.notEqual(i, dormitoryIndex));
        }
    }

    private Map<Student, Dormitory> runGaleShapley() {
        Map<Student, Dormitory> matching = new HashMap<>();
        Queue<Student> freeStudents = new LinkedList<>(students);

        while (!freeStudents.isEmpty()) {
            Student student = freeStudents.poll();
            for (Dormitory dormitory : student.getPreferences()) {
                if (dormitory.getStudents().size() < dormitory.getCapacity()) {
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
        Solution solution = new Solution(students.size(), 1, 1);
        for (int i = 0; i < students.size(); i++) {
            solution.setVariable(i, EncodingUtils.newInt(-1, dormitories.size() - 1));
        }
        return solution;
    }

}