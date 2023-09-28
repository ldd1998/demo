package org.example.service.regex;

import java.util.regex.*;

public class DeviceNameMatcher {
    public static void main(String[] args) {
        String input = "台州.步路变/#2所用变";
        Pattern pattern = Pattern.compile("(?<=/)[^/]+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String result = matcher.group();
            System.out.println(result);
        }
    }
}
