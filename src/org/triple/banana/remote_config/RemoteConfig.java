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
    private static final Executor WORKER_THREAD = Executors.newSingleThreadExecutor();
    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    private final String mRemoteConfigUrl;
    private JSONObject mCache;

    public interface JsonCallback { void onResult(JSONObject json); }

    public RemoteConfig(String url) {
        mRemoteConfigUrl = url;
    }

    public void getAsync(final JsonCallback callback) {
        if (mCache != null && mCache.length() != 0) {
            callback.onResult(mCache);
            return;
        }

        WORKER_THREAD.execute(() -> {
            final JSONObject json = get();
            MAIN_THREAD.post(() -> {
                mCache = json;
                callback.onResult(json);
            });
        });
    }

    public JSONObject get() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mRemoteConfigUrl);

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

    private String getStringFromStream(InputStream inputStream) {
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
            Log.e(TAG, "getStringFromStream(): " + e.toString());
        }
        return new String();
    }

    private JSONObject getJSONObjectFromStream(InputStream inputStream) throws Exception {
        return new JSONObject(getStringFromStream(inputStream));
    }
}
