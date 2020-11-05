// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.subresource_filter;

import static org.triple.banana.subresource_filter.ruleset.RulesetVersion.BUILTIN_RULESET_SIZE;
import static org.triple.banana.subresource_filter.ruleset.RulesetVersion.BUILTIN_RULESET_VERSION;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaSubresourceFilter;
import org.triple.banana.download.SimpleDownloader;
import org.triple.banana.remote_config.RemoteConfig;
import org.triple.banana.util.Unzip;

import java.io.File;
import java.io.InputStream;

public enum RulesetLoader {
    instance;

    private static final String TAG = "RulesetLoader";
    private final RemoteConfig mMetaData =
            new RemoteConfig("https://triplebanana.github.io/filter/metadata.json");

    private static final Uri RULESET_BASE_URL = Uri.parse("https://triplebanana.github.io/filter/");
    private static final String LAST_CHECK_TIME_KEY = "filter_download_last_check_time";
    private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_DAY * 1;

    private boolean isUpdateCheckTimeExpired() {
        long now = System.currentTimeMillis();
        long lastCheckTime =
                BananaApplicationUtils.get().getSharedPreferences().getLong(LAST_CHECK_TIME_KEY, 0);
        return now >= lastCheckTime + UPDATE_INTERVAL;
    }

    private void resetUpdateCheckTime() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.remove(LAST_CHECK_TIME_KEY);
        editor.apply();
    }

    private void setLastUpdateCheckTime() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putLong(LAST_CHECK_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    private boolean updateBuiltInRuleset() {
        String currentVersion = getVersion();
        if (getVersionNumber(currentVersion) >= getVersionNumber(BUILTIN_RULESET_VERSION)) {
            Log.d(TAG, "updateBuiltInRuleset(): It is already latest version = " + currentVersion);
            return false;
        }

        Log.d(TAG, "updateBuiltInRuleset(): VERSION = " + BUILTIN_RULESET_VERSION);
        Log.d(TAG, "updateBuiltInRuleset(): SIZE = " + BUILTIN_RULESET_SIZE);

        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return false;

        AssetManager manager = context.getAssets();
        try (InputStream stream = manager.open(BUILTIN_RULESET_VERSION + ".filter.zip")) {
            File destinationDir = context.getExternalFilesDir(null);
            File destinationFile = new File(destinationDir, BUILTIN_RULESET_VERSION + ".filter");
            Log.d(TAG, "updateBuiltInRuleset(): destinationFile = " + destinationFile);

            if (!Unzip.get().extract(stream, destinationDir, true)) {
                Log.e(TAG, "updateBuiltInRuleset(): Extracting failed");
                return false;
            }

            if (destinationFile.length() != BUILTIN_RULESET_SIZE) {
                Log.e(TAG,
                        "updateBuiltInRuleset(): The ruleset is corrupted "
                                + destinationFile.length());
                return false;
            }

            String builtInRulesetPath = destinationFile.getPath();
            installRuleset(builtInRulesetPath, () -> { updateRemoteRuleset(); });
            return true;
        } catch (Exception e) {
            Log.e(TAG, "updateBuiltInRuleset(): " + e.toString());
        }
        return false;
    }

    private void updateRemoteRuleset() {
        if (!isUpdateCheckTimeExpired()) {
            Log.d(TAG, "updateRemoteRuleset(): Skip checkUpdate()");
            return;
        }

        checkUpdate(getVersion());
    }

    private void checkUpdate(final String currentVersion) {
        Log.d(TAG, "checkUpdate(): Request checkUpdate");
        mMetaData.getAsync(info -> {
            String newVersion = info.optString("version");
            long rulesetSize = info.optLong("size");
            Log.d(TAG, "checkUpdate(): current = " + currentVersion + ", new = " + newVersion);

            boolean isMetaDataParsingFailed = TextUtils.isEmpty(newVersion) || rulesetSize == 0;
            if (isMetaDataParsingFailed) {
                Log.e(TAG, "checkUpdate(): Parsing metadata failed");
                return;
            }

            if (getVersionNumber(currentVersion) >= getVersionNumber(newVersion)) {
                setLastUpdateCheckTime();
                Log.d(TAG, "checkUpdate(): It is already latest version = " + currentVersion);
                return;
            }

            // Delegate handling the callback to downloadLatestRuleset()
            downloadLatestRuleset(currentVersion, newVersion, rulesetSize);
        });
    }

    private void downloadLatestRuleset(
            final String currentVersion, final String newVersion, final long rulesetSize) {
        Log.d(TAG,
                "downloadLatestRuleset(): current = " + currentVersion + ", new = " + newVersion);
        Uri rulesetUri = Uri.withAppendedPath(RULESET_BASE_URL, newVersion + ".filter.zip");
        SimpleDownloader.get().download(rulesetUri, compressedRuleset -> {
            if (compressedRuleset == null) {
                Log.e(TAG, "downloadLatestRuleset(): Downloading failed");
                return;
            }

            Log.d(TAG, "downloadLatestRuleset(): Extract " + compressedRuleset);
            if (!Unzip.get().extract(compressedRuleset, true)) {
                Log.e(TAG, "downloadLatestRuleset(): Extracting failed");
                return;
            }

            File rulesetFile = new File(compressedRuleset.getPath().replace(".zip", ""));
            if (rulesetFile.length() != rulesetSize) {
                Log.e(TAG,
                        "downloadLatestRuleset(): The ruleset is corrupted" + rulesetFile.length());
                return;
            }

            compressedRuleset.delete();
            installRuleset(rulesetFile.getPath(), () -> { setLastUpdateCheckTime(); });
        });
    }

    private long getVersionNumber(@NonNull String versionString) {
        if (TextUtils.isEmpty(versionString)) return 0;

        try {
            return Long.parseLong(versionString.replace(".", ""));
        } catch (Exception e) {
            Log.e(TAG, "getVersionNumber(): Parsing error \"" + versionString + "\"");
        }
        return 0;
    }

    private void installRuleset(@NonNull String rulesetPath, @NonNull Runnable successCallback) {
        Log.d(TAG, "installRuleset(): Request to install = " + rulesetPath);
        BananaSubresourceFilter.get().install(rulesetPath, () -> {
            Log.d(TAG, "installRuleset(): Installed = " + getVersion());
            successCallback.run();
        });
    }

    private void resetRuleset() {
        BananaSubresourceFilter.get().reset();
    }

    private @NonNull String getVersion() {
        return BananaSubresourceFilter.get().getVersion();
    }

    /**
     * This is called if the application is updated or users request to reset/update the ruleset by
     * force(it can be triggered via long-click event on update ruleset preference).
     */
    public void forceUpdateRuleset() {
        resetUpdateCheckTime();
        resetRuleset();
        updateRulesetIfNeeded();
    }

    /**
     * This is called if users request to update the remote ruleset by force(it can be triggered via
     * click event on update ruleset preference).
     */
    public void forceUpdateRemoteRuleset() {
        resetUpdateCheckTime();
        updateRemoteRuleset();
    }

    /**
     * This is called if the application is launched.
     */
    public void updateRulesetIfNeeded() {
        if (updateBuiltInRuleset()) return;
        updateRemoteRuleset();
    }
}
