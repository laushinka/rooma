package com.rooma.scraper.search;

public class Utils {
    public static String decodeStr(String text) {
        final char[] UNICODE_PREFIX = "\\u".toCharArray();
        final int UNICODE_LENGTH = 4; // 2 bytes

        StringBuilder sb = new StringBuilder();
        char[] chars = text.toCharArray();

        for (int index = 0; index < chars.length; index++) {
            int remaining = chars.length - index;
            char c = chars[index];

            if (remaining < UNICODE_PREFIX.length + UNICODE_LENGTH) {
                sb.append(c);
                continue;
            }

            if (chars[index] == UNICODE_PREFIX[0] && chars[index+1] == UNICODE_PREFIX[1]) {
                int charStart = index + UNICODE_PREFIX.length;
                int charEnd = charStart + UNICODE_LENGTH;

                int value = Integer.parseInt(text.substring(charStart, charEnd), 16);

                c = (char)value;
                index = charEnd - 1;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
