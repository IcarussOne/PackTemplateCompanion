package com.barebonium.packcompanion.utils;

import java.util.regex.Pattern;

public class MessageRegex {
    private static final Pattern LINK_PATTERN = Pattern.compile("#link:\\[([^]]+)]\\(([^)]+)\\)");
    private static final Pattern CODE_PATTERN = Pattern.compile("`([^`]+)`");
    public static String translateToMarkdown(String message) {
        if (message == null) return "-";

        return LINK_PATTERN.matcher(message).replaceAll("[$1]($2)");
    }


    public static String translateToHTML(String message) {
        if (message == null) return "-";
        String result = LINK_PATTERN.matcher(message).replaceAll("<a href=\"$2\" target=\"_blank\">$1</a>");
        return CODE_PATTERN.matcher(result).replaceAll("<span class=\"code-snippet\">$1</span>");
    }
}
