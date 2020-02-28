// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.remote_config;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RemoteConfig {
    private static final String TAG = "RemoteConfig";
    private static final String REMOTE_CONFIG_URL =
            "https://zino.dev/triple_banana_config/remote_config.json";
    private static final Executor WORKER_THREAD = Executors.newSingleThreadExecutor();
    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    private static JSONObject sCache;

    private interface JsonCallback { void onResult(JSONObject json); }

    private static void getJsonFromUrlAsync(final String urlString, final JsonCallback callback) {
        if (sCache != null && sCache.length() != 0) {
            callback.onResult(sCache);
            return;
        }

        WORKER_THREAD.execute(() -> {
            final JSONObject json = getJsonFromUrl(urlString);
            MAIN_THREAD.post(() -> {
                sCache = json;
                callback.onResult(json);
            });
        });
    }

    private static JSONObject getJsonFromUrl(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            try (InputStream stream = connection.getInputStream()) {
                return getJSONObjectFromStream(stream);
            }
        } catch (Exception e) {
            Log.e(TAG, "getJsonFromUrl(): " + e.toString());
        } finally {
            if (connection != null) connection.disconnect();
        }
        return new JSONObject();
    }

    private static String getStringFromStream(InputStream inputStream) {
        if (inputStream == null) return new String();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toString("UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "readStringFromStream(): " + e.toString());
        }
        return new String();
    }

    private static JSONObject getJSONObjectFromStream(InputStream inputStream) throws Exception {
        return new JSONObject(getStringFromStream(inputStream));
    }

    public interface BooleanCallback { void onResult(boolean result); }

    public static void getBoolean(final String key, final BooleanCallback callback) {
        getJsonFromUrlAsync(REMOTE_CONFIG_URL, json -> {
            try {
                callback.onResult(json.has(key) && json.getBoolean(key));
            } catch (Exception e) {
                Log.e(TAG, "getBoolean(): " + e.toString());
                callback.onResult(false);
            }
        });
    }
}
