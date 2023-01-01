package io.github.ohmry.utils;

public class StringUtils {
    public static boolean hasText(String value) {
        if (value == null) {
            return false;
        } else if (value.length() < 1) {
            return false;
        } else {
            return true;
        }
    }
}
