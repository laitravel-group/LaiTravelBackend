package com.laitravel.laitravelbe.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GsonUtil {
    public static Gson gson = new GsonBuilder()
            .create();

}
