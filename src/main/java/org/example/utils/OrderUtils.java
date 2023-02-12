package org.example.utils;

import java.util.List;

public class OrderUtils {
    public static List<String> getCorrectIngredients() {
        return List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");
    }

    public static List<String> getIncorrectIngredients() {
        return List.of("string1", "string2");
    }
}
