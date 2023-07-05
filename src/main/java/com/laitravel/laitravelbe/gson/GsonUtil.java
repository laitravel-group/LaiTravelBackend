package com.laitravel.laitravelbe.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GsonUtil {
    public static Gson gson = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategies()).create();

    private static class CustomExclusionStrategies implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            Field field = null;
            try {
                field = f.getClass().getDeclaredField("field");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            return Modifier.isPrivate(modifiers);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return true;
        }

    }
}
