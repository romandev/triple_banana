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

import org.banana.cake.interfaces.BananaJavaScriptCallback;
import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;

import java.net.MalformedURLException;
import java.net.URL;

public class YouTubeUtil {
    private static final String TAG = "YouTubeUtil";

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
        // NOTIMPLEMENTED
    }

    public static void dismissQualityOptionsDialog() {
        // NOTIMPLEMENTED
    }

    private static void getQualityInfos(@NonNull BananaJavaScriptCallback callback) {
        // NOTIMPLEMENTED
    }

    private static void setQuality(@NonNull String quality) {
        // NOTIMPLEMENTED
    }

    private static void runScriptOnYouTube(
            @NonNull String script, @NonNull BananaJavaScriptCallback callback) {
        // NOTIMPLEMENTED
    }
}
