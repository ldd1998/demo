package org.example.util;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author chatGpt
 */
public class EntityRandomizer {
    private static final Random random = new Random();

    public static <T> T getRandomizedEntity(Class<T> entityType) {
        T entity = null;
        try {
            entity = entityType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (java.lang.reflect.Field field : entityType.getDeclaredFields()) {
            field.setAccessible(true);
            Object randomValue = getRandomValue(field.getType());
            try {
                field.set(entity, randomValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return entity;
    }

    private static Object getRandomValue(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return random.nextInt();
        } else if (type == long.class || type == Long.class) {
            return random.nextLong();
        } else if (type == double.class || type == Double.class) {
            return random.nextDouble();
        } else if (type == float.class || type == Float.class) {
            return random.nextFloat();
        } else if (type == boolean.class || type == Boolean.class) {
            return random.nextBoolean();
        } else if (type == String.class) {
            return generateRandomString();
        } else if (type == LocalDateTime.class){
            return LocalDateTime.now();
        }else {
            // For other types, return null or handle as per your requirement
            return null;
        }
    }

    private static String generateRandomString() {
        int length = 10; // Set the desired length of the random string
        StringBuilder sb = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
