package io.github.ohmry.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {
    public static String getString(String contents, String key) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\":\\\"(.+?)\\\"");
        Matcher matcher = pattern.matcher(contents);
        return !matcher.find() ? null : matcher.group(1);
    }

    public static Integer getInteger(String contents, String key) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\":(.+?)");
        Matcher matcher = pattern.matcher(contents);
        return !matcher.find() ? null : Integer.parseInt(matcher.group(1));
    }

    public static List<String> getArray(String contents, String key) {
        Pattern pattern = Pattern.compile("\\\"" + key + "\\\":\\[(\\{.+?\\})\\]");
        Matcher matcher = pattern.matcher(contents);

        if (!matcher.find()) return null;
        String arrayString = matcher.group(1);
        pattern = Pattern.compile("\\{.+?\\}");
        matcher = pattern.matcher(arrayString);

        List<String> response = new ArrayList<>();
        while (matcher.find()) {
            response.add(matcher.group(0).replaceAll("\\\\", ""));
        }
        return response;
    }

    public static LocalDateTime getLocalDateTime(String contents, String key, String format) {
        String value = JsonUtils.getString(contents, key);
        return value == null ? null : LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(value));
    }
}
