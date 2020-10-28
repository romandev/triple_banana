// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaJavaScriptCallback;
import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YouTubeUtil {
    private static final String TAG = "YouTubeUtil";
    // FIXME: Consider a way to prevent memory leak.
    private static AlertDialog sAlertDialog;

    private static class ArrayListMap<K, V> {
        private final @NonNull Map<K, V> mMap = new HashMap<>();
        private final @NonNull Map<K, Integer> mIndexMap = new HashMap<>();
        private final @NonNull ArrayList<K> mList = new ArrayList<>();

        ArrayListMap() {}

        void put(@NonNull K key, @NonNull V value) {
            if (mMap.containsKey(key)) {
                // FIXME: Currently, we don't support duplicated key for now.
                return;
            }

            mMap.put(key, value);
            mIndexMap.put(key, mList.size());
            mList.add(key);
        }

        @Nullable
        V get(@NonNull K key) {
            return mMap.get(key);
        }

        int getIndexOfKey(@NonNull K key) {
            return mIndexMap.get(key);
        }

        @Nullable
        K getKeyByIndex(int index) {
            return mList.get(index);
        }

        @NonNull
        final ArrayList<K> getKeyList() {
            return mList;
        }
    }

    private static class QualityInfo {
        private static final @NonNull HashMap<String, String> QUALITY_MAP =
                new HashMap<String, String>() {
                    {
                        put("auto", "auto");
                        put("tiny", "144p");
                        put("small", "240p");
                        put("medium", "360p");
                        put("large", "480p");
                        put("hd720", "720p");
                        put("hd1080", "1080p");
                        put("hd1440", "1440p");
                        put("hd2160", "2160p");
                        put("hd2880", "2880p");
                        put("highres", "4320p");
                    }
                };
        private final @NonNull ArrayListMap<String, String> mAvailableQualities =
                new ArrayListMap<>();
        private @NonNull String mPreferredQuality = "";

        QualityInfo() {}

        void addAvailableQuality(@NonNull String quality) {
            if (QUALITY_MAP.containsKey(quality)) {
                mAvailableQualities.put(quality, QUALITY_MAP.get(quality));
            }
        }

        void setPreferredQuality(@NonNull String quality) {
            mPreferredQuality = quality;
        }

        @NonNull
        String[] toStringArray() {
            ArrayList<String> keyList = mAvailableQualities.getKeyList();
            String[] result = new String[keyList.size()];
            for (int i = 0; i < keyList.size(); i++) {
                String availableQuality = keyList.get(i);
                String label = mAvailableQualities.get(availableQuality);
                result[i] = availableQuality + " (" + label + ")";
            }
            return result;
        }

        int getIndexOfPreferredQuality() {
            return mAvailableQualities.getIndexOfKey(mPreferredQuality);
        }

        String getQualityByIndex(int index) {
            return mAvailableQualities.getKeyByIndex(index);
        }
    }

    public static boolean isYouTubeUrl() {
        BananaTab tab = BananaTabManager.get().getActivityTab();
        if (tab == null) {
            return false;
        }
        return isYouTubeUrl(tab);
    }

    public static boolean isYouTubeUrl(BananaTab tab) {
        String urlString = tab.getUrl();
        if (TextUtils.isEmpty(urlString)) {
            return false;
        }
        try {
            URL url = new URL(urlString);
            if ("/watch".equalsIgnoreCase(url.getPath())) {
                final String host = url.getHost();
                if ("www.youtube.com".equalsIgnoreCase(host) || "youtube.com".equalsIgnoreCase(host)
                        || "m.youtube.com".equalsIgnoreCase(host)) {
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException " + e.getMessage());
        }
        return false;
    }

    public static void showQualityOptionsDialog(@Nullable Context context) {
        if (context == null || !isYouTubeUrl()) {
            return;
        }

        getQualityInfo((jsonString) -> {
            if (TextUtils.isEmpty(jsonString)) {
                return;
            }
            QualityInfo info = parseQualityInfo(jsonString);
            sAlertDialog = BananaApplicationUtils.get()
                                   .getDialogBuilder(context)
                                   .setNegativeButton(android.R.string.cancel, null)
                                   .setSingleChoiceItems(info.toStringArray(),
                                           info.getIndexOfPreferredQuality(),
                                           (dialog, which) -> {
                                               setQuality(info.getQualityByIndex(which));
                                               dialog.dismiss();
                                           })
                                   .create();
            sAlertDialog.show();
        });
    }

    public static void dismissQualityOptionsDialog() {
        if (sAlertDialog == null || !sAlertDialog.isShowing()) {
            return;
        }
        sAlertDialog.dismiss();
    }

    private static @NonNull QualityInfo parseQualityInfo(@NonNull String jsonString) {
        QualityInfo info = new QualityInfo();
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray availableQualities = json.getJSONArray("availableQualities");
            for (int i = 0; i < availableQualities.length(); i++) {
                info.addAvailableQuality(availableQualities.getString(i));
            }
            info.setPreferredQuality(json.getString("preferredQuality"));
        } catch (Exception e) {
            Log.e(TAG, "parseQualityInfo(): " + e.toString());
        }
        return info;
    }

    private static void getQualityInfo(@NonNull BananaJavaScriptCallback callback) {
        String script = ""
                + "Object("
                + "  {"
                + "    preferredQuality: node.getPreferredQuality(),"
                + "    availableQualities: node.getAvailableQualityLevels()"
                + "  }"
                + ")";
        runScriptOnYouTube(script, callback);
    }

    private static void setQuality(@NonNull String quality) {
        String script = ""
                + "node.setPlaybackQualityRange('" + quality + "');"
                + "node.setPlaybackQuality('" + quality + "');";
        runScriptOnYouTube(script, null);
    }

    private static void runScriptOnYouTube(
            @NonNull String script, @NonNull BananaJavaScriptCallback callback) {
        BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
        if (tab == null) return;
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("node = document.querySelector('.html5-video-player');");
        scriptBuilder.append(script);
        tab.evaluateJavaScript(scriptBuilder.toString(), callback);
    }
}
