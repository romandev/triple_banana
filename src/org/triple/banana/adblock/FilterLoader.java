// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.adblock;

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
import org.triple.banana.version.VersionInfo;

import java.io.File;
import java.io.InputStream;

public enum FilterLoader {
    instance;

    private static final String TAG = "FilterLoader";
    private final RemoteConfig mMetaData =
            new RemoteConfig("https://triplebanana.github.io/filter/metadata.json");

    private static final Uri FILTER_BASE_URL = Uri.parse("https://triplebanana.github.io/filter/");
    private static final String LAST_CHECK_TIME_KEY = "filter_download_last_check_time";
    private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_DAY * 1;
    private static final String NO_FILTER = new String();
    private static final String BUILTIN_FILTER_VERSION = "1.0.0.00003";
    private static final long BUILTIN_FILTER_SIZE = 2334058;

    private long getLastCheckTime() {
        return BananaApplicationUtils.get().getSharedPreferences().getLong(LAST_CHECK_TIME_KEY, 0);
    }

    private void updateLastCheckTime() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putLong(LAST_CHECK_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    private String installBuiltInFilter() {
        Log.d(TAG, "installBuiltInFilter(): VERSION = " + BUILTIN_FILTER_VERSION);
        Log.d(TAG, "installBuiltInFilter(): SIZE = " + BUILTIN_FILTER_SIZE);

        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return NO_FILTER;

        AssetManager manager = context.getAssets();
        try (InputStream stream = manager.open(BUILTIN_FILTER_VERSION + ".filter.zip")) {
            File destinationDir = context.getExternalFilesDir(null);
            File destinationFile = new File(destinationDir, BUILTIN_FILTER_VERSION + ".filter");
            Log.d(TAG, "installBuiltInFilter(): destinationFile = " + destinationFile);

            if (!Unzip.get().extract(stream, destinationDir, true)) {
                Log.e(TAG, "installBuiltInFilter(): Extracting failed");
                return NO_FILTER;
            }

            if (destinationFile.length() != BUILTIN_FILTER_SIZE) {
                Log.e(TAG,
                        "installBuiltInFilter(): The filter is corrupted "
                                + destinationFile.length());
                return NO_FILTER;
            }

            VersionInfo.setFilterVersion(BUILTIN_FILTER_VERSION);
            Log.d(TAG, "installBuiltInFilter(): Installed = " + BUILTIN_FILTER_VERSION);
            return destinationFile.getPath();
        } catch (Exception e) {
            Log.e(TAG, "installBuiltInFilter(): " + e.toString());
            return NO_FILTER;
        }
    }

    private void updateLatestFilter(
            final String currentVersion, final String newVersion, final long filterSize) {
        Log.d(TAG, "updateLatestFilter(): current = " + currentVersion + ", new = " + newVersion);
        Uri filterUri = Uri.withAppendedPath(FILTER_BASE_URL, newVersion + ".filter.zip");
        SimpleDownloader.get().download(filterUri, compressedFilter -> {
            if (compressedFilter == null) {
                Log.e(TAG, "updateLatestFilter(): Downloading failed");
                return;
            }

            Log.d(TAG, "updateLatestFilter(): Extract " + compressedFilter);
            if (!Unzip.get().extract(compressedFilter, true)) {
                Log.e(TAG, "updateLatestFilter(): Extracting failed");
                return;
            }

            File filterFile = new File(compressedFilter.getPath().replace(".zip", ""));
            if (filterFile.length() != filterSize) {
                Log.e(TAG, "updateLatestFilter(): The filter is corrupted" + filterFile.length());
                return;
            }

            compressedFilter.delete();
            VersionInfo.setFilterVersion(newVersion);
            updateLastCheckTime();
            Log.d(TAG, "updateLatestFilter(): Installed = " + newVersion);

            installFilter(filterFile.getPath());
        });
    }

    private void checkUpdate(final String currentVersion) {
        mMetaData.getAsync(info -> {
            String newVersion = info.optString("version");
            long filterSize = info.optLong("size");
            Log.d(TAG, "checkUpdate(): current = " + currentVersion + ", new = " + newVersion);

            boolean isMetaDataParsingFailed = TextUtils.isEmpty(newVersion) || filterSize == 0;
            if (isMetaDataParsingFailed) {
                Log.e(TAG, "checkUpdate(): Parsing metadata failed");
                VersionInfo.setFilterVersion(currentVersion);
                return;
            }

            if (getVersionNumber(currentVersion) >= getVersionNumber(newVersion)) {
                VersionInfo.setFilterVersion(currentVersion);
                updateLastCheckTime();
                return;
            }

            // Delegate handling the callback to updateLatestFilter()
            updateLatestFilter(currentVersion, newVersion, filterSize);
        });
    }

    private long getVersionNumber(final String versionString) {
        return Long.parseLong(versionString.replace(".", ""));
    }

    private void installFilter(@NonNull String rulesetPath) {
        BananaSubresourceFilter.get().install(rulesetPath);
    }

    private void reset() {
        BananaSubresourceFilter.get().reset();
    }

    private @NonNull String getVersion() {
        return BananaSubresourceFilter.get().getVersion();
    }

    public void forceUpdate() {
        reset();
        updateIfNeeded();
    }

    public void updateIfNeeded() {
        String currentVersion = getVersion();
        Log.d(TAG, "updateIfNeeded(): currentVersion = " + currentVersion);
        boolean hasInstalledFilter = !TextUtils.isEmpty(currentVersion);
        boolean needToUpdateBuiltInFilter = !hasInstalledFilter
                || getVersionNumber(currentVersion) < getVersionNumber(BUILTIN_FILTER_VERSION);
        if (needToUpdateBuiltInFilter) {
            String builtInFilter = installBuiltInFilter();
            if (!TextUtils.isEmpty(builtInFilter)) {
                installFilter(builtInFilter);
                return;
            }
        }

        long now = System.currentTimeMillis();
        boolean isUpdateCheckTimeExpired = now >= getLastCheckTime() + UPDATE_INTERVAL;
        if (hasInstalledFilter && !isUpdateCheckTimeExpired) {
            Log.d(TAG, "updateIfNeeded(): Skip checkUpdate()");
            VersionInfo.setFilterVersion(currentVersion);
            return;
        }

        // Delegate handling the callback to checkUpdate()
        checkUpdate(currentVersion);
    }
}
