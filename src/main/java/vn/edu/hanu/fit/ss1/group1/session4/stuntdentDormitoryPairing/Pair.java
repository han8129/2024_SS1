package vn.edu.hanu.fit.ss1.group1.session4.stuntdentDormitoryPairing;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
public class Pair<K, V> {
    K key;
    V value;
}
