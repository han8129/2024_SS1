package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Dormitory {

    String name;
    List<Student> preferences; // List of preferred students
    int capacity;
    List<Student> students; // List of students assigned to this dormitory
    String dormTrait; // Specific trait or characteristic of the dormitory
    List<String> studentPreferences; // Specific traits/preferences the dormitory is looking for in students

    public Dormitory(String name, List<Student> preferences, int capacity, String dormTrait, List<String> studentPreferences) {
        if (name == null || preferences == null || dormTrait == null || studentPreferences == null) {
            throw new IllegalArgumentException("Name, preferences, dorm trait, and student preferences cannot be null");
        }
        this.name = name;
        this.preferences = preferences;
        this.capacity = capacity;
        this.dormTrait = dormTrait;
        this.studentPreferences = studentPreferences;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        students.add(student);
    }

    public void removeStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        students.remove(student);
    }
}

