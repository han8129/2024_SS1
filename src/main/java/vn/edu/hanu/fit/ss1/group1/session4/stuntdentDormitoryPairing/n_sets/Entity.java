package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing.n_sets;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Entity {
    String name;
    String type;
    int capacity;
    List<Entity> preferences;

    public Entity(String name, String type, int capacity) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.preferences = new ArrayList<>();
    }

    public void setPreferences(List<Entity> preferences) {
        this.preferences.clear();
        this.preferences.addAll(preferences);
    }
}
