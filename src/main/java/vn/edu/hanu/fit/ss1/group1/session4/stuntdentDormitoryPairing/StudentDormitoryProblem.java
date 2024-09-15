package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import org.moeaframework.problem.AbstractProblem;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.List;

public class StudentDormitoryProblem extends AbstractProblem {
    private final List<Student> students;
    private final List<Dormitory> dormitories;
    public StudentDormitoryProblem(List<Student> students, List<Dormitory> dormitories) {
        // The number of variables is equal to the number of students
        super(students.size(), 3);
        this.students = students;
        this.dormitories = dormitories;
    }
    @Override
    public void evaluate(Solution solution) {
        int studentsWithoutDormitory = 0;
        int studentsInOvercrowdedDormitory = 0;
        int preferenceMismatch = 0;
        for (Dormitory dormitory : dormitories) dormitory.getStudents().clear();
        for (Student student : students) student.getPreferences().clear();
        // Assign students to dormitories based on the solution's variables
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
        }
        solution.setObjective(0, studentsWithoutDormitory);
        solution.setObjective(1, studentsInOvercrowdedDormitory);
        solution.setObjective(2, preferenceMismatch);
    }
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(students.size(), 3);
        for (int i = 0; i < students.size(); i++) solution.setVariable(i, EncodingUtils.newInt(-1, dormitories.size() - 1));
        return solution;
    }
}

