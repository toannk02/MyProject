package com.example.myproject.util;

import java.lang.reflect.Field;
import java.util.Random;

public class Constants {
    public static class DefaultValuePage {
        public static final Integer PAGE_INDEX = 1;
        public static final Integer PAGE_SIZE = 5;
        public static final String SORT_BY = "id";
    }

    public static class status {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String PENDING = "PENDING";
        public static final String ORDERED = "ORDERED";
    }

    public static String validateOrder(String orderBy) {
        if (orderBy != null && orderBy.equalsIgnoreCase("asc"))
            return "ASC";
        return "DESC";
    }

    public static <T> String validateSort(String sortBy, String defaultKey, Field[] fields) {
        if (sortBy != null)
            for (Field field : fields)
                if (field.getName().equalsIgnoreCase(sortBy))
                    return field.getName();

        return defaultKey;
    }

    public static String randomPassword(String name) {
        name = name + "123456";
        return name;
    }

    private static String generateRandomString(int length, String characters) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        Random random = new Random();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}
