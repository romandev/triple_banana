// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.youtube;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaJavaScriptCallback;
import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class YouTubePlayerController {
    private final @NonNull YouTubeCommandBuilder mCommandBuilder;

    public static interface ArrayCallback { void onResult(@NonNull JSONArray array); }

    public static interface ObjectCallback { void onResult(@NonNull JSONObject object); }

    public YouTubePlayerController() {
        mCommandBuilder = new YouTubeCommandBuilder();
    }

    public void getAvailableCaptionList(final @NonNull ArrayCallback callback) {
        String command = mCommandBuilder.getOption("captions", "tracklist");
        runCommand(command, result -> {
            try {
                callback.onResult(new JSONArray(result));
            } catch (Exception e) {
                callback.onResult(new JSONArray());
            }
        });
    }

    public void getCurrentCaption(final @NonNull ObjectCallback callback) {
        String command = mCommandBuilder.getOption("captions", "track");
        runCommand(command, result -> {
            try {
                callback.onResult(new JSONObject(result));
            } catch (Exception e) {
                callback.onResult(new JSONObject());
            }
        });
    }

    public void setCaption(@NonNull String languageCode) {
        JSONObject value = new JSONObject();
        try {
            if (!TextUtils.isEmpty(languageCode)) {
                value.put("languageCode", languageCode);
            }
        } catch (Exception e) {
        }
        String command = mCommandBuilder.setOption("captions", "track", value);
        runCommand(command, result -> {});
    }

    private void runCommand(@NonNull String command, @NonNull BananaJavaScriptCallback callback) {
        BananaTab tab = BananaTabManager.get().getActivityTab();
        if (tab == null) {
            callback.handleJavaScriptResult("");
            return;
        }
        tab.evaluateJavaScript(command, callback);
    }
}
