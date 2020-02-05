// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BananaInterfaceProvider {
    public interface Supplier<T> { T get(); }

    private static final String TAG = "BananaInterfaceProvider";
    private static final Map<Class, Supplier> CONSTRUCTOR_MAP = new HashMap<>();
    private static final Map<Class, Object> INSTANCE_MAP = new HashMap<>();

    private static boolean isSingleton(Class interfaceClass) {
        return INSTANCE_MAP.containsKey(interfaceClass);
    }

    private static <T> T create(Class<T> interfaceClass) {
        try {
            return (T) CONSTRUCTOR_MAP.get(interfaceClass).get();
        } catch (Exception e) {
            Log.e(TAG, "create(): " + e.toString());
        }
        return null;
    }

    private static <T> T getOrCreate(Class<T> interfaceClass) {
        try {
            T instance = (T) INSTANCE_MAP.get(interfaceClass);
            if (instance == null) {
                instance = create(interfaceClass);
                INSTANCE_MAP.put(interfaceClass, instance);
            }
            return instance;
        } catch (Exception e) {
            Log.e(TAG, "getOrCreate(): " + e.toString());
        }
        return null;
    }

    public enum InstanceType { NORMAL, SINGLETON }

    public static <T> void register(
            Class<T> interfaceClass, Supplier<? extends T> constructor, InstanceType type) {
        CONSTRUCTOR_MAP.put(interfaceClass, constructor);
        if (type == InstanceType.SINGLETON) INSTANCE_MAP.put(interfaceClass, null);
    }

    public static <T> void register(Class<T> interfaceClass, Supplier<? extends T> constructor) {
        register(interfaceClass, constructor, InstanceType.NORMAL);
    }

    public static <T> T get(Class<T> interfaceClass) {
        try {
            if (isSingleton(interfaceClass)) return getOrCreate(interfaceClass);
            return create(interfaceClass);
        } catch (Exception e) {
            Log.e(TAG, "get(): " + e.toString());
        }
        return null;
    }
}
