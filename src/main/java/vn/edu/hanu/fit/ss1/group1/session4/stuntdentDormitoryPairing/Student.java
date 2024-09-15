package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Student {
    String name;
    List<Dormitory> preferences; // List of preferred dormitories
    String personalityTrait; // Personality trait of the student
    List<String> dormPreferences; // Specific traits/preferences the student is looking for in a dormitory

    // Constructor with parameters
    public Student(String name, List<Dormitory> preferences, String personalityTrait, List<String> dormPreferences) {
        if (name == null || preferences == null || personalityTrait == null || dormPreferences == null) {
            throw new IllegalArgumentException("Name, preferences, personality trait, and dorm preferences cannot be null");
        }
        this.name = name;
        this.preferences = preferences;
        this.personalityTrait = personalityTrait;
        this.dormPreferences = dormPreferences;
    }

}
