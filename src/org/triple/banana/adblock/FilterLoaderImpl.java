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

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.adblock.mojom.FilterLoader;
import org.triple.banana.download.SimpleDownloader;
import org.triple.banana.remote_config.RemoteConfig;
import org.triple.banana.util.Unzip;
import org.triple.banana.version.VersionInfo;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

import java.io.File;
import java.io.InputStream;

public class FilterLoaderImpl implements FilterLoader {
    private static final String TAG = "FilterLoaderImpl";
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

    private void updateLatestFilter(final String currentVersion, final String newVersion,
            final long filterSize, final LoadResponse callback) {
        Log.d(TAG, "updateLatestFilter(): current = " + currentVersion + ", new = " + newVersion);
        Uri filterUri = Uri.withAppendedPath(FILTER_BASE_URL, newVersion + ".filter.zip");
        SimpleDownloader.get().download(filterUri, compressedFilter -> {
            if (compressedFilter == null) {
                Log.e(TAG, "updateLatestFilter(): Downloading failed");
                callback.call(NO_FILTER);
                return;
            }

            Log.d(TAG, "updateLatestFilter(): Extract " + compressedFilter);
            if (!Unzip.get().extract(compressedFilter, true)) {
                Log.e(TAG, "updateLatestFilter(): Extracting failed");
                callback.call(NO_FILTER);
                return;
            }

            File filterFile = new File(compressedFilter.getPath().replace(".zip", ""));
            if (filterFile.length() != filterSize) {
                Log.e(TAG, "updateLatestFilter(): The filter is corrupted" + filterFile.length());
                callback.call(NO_FILTER);
                return;
            }

            compressedFilter.delete();
            VersionInfo.setFilterVersion(newVersion);
            updateLastCheckTime();
            Log.d(TAG, "updateLatestFilter(): Installed = " + newVersion);

            callback.call(filterFile.getPath());
        });
    }

    private void checkUpdate(final String currentVersion, final LoadResponse callback) {
        mMetaData.getAsync(info -> {
            String newVersion = info.optString("version");
            long filterSize = info.optLong("size");
            Log.d(TAG, "checkUpdate(): current = " + currentVersion + ", new = " + newVersion);

            boolean isMetaDataParsingFailed = TextUtils.isEmpty(newVersion) || filterSize == 0;
            if (isMetaDataParsingFailed) {
                Log.e(TAG, "checkUpdate(): Parsing metadata failed");
                VersionInfo.setFilterVersion(currentVersion);
                callback.call(NO_FILTER);
                return;
            }

            if (TextUtils.equals(currentVersion, newVersion)) {
                VersionInfo.setFilterVersion(currentVersion);
                updateLastCheckTime();
                callback.call(NO_FILTER);
                return;
            }

            // Delegate handling the callback to updateLatestFilter()
            updateLatestFilter(currentVersion, newVersion, filterSize, callback);
        });
    }

    private long getVersionNumber(final String versionString) {
        return Long.parseLong(versionString.replace(".", ""));
    }

    @Override
    public void load(final String currentVersion, final LoadResponse callback) {
        Log.d(TAG, "load(): currentVersion = " + currentVersion);
        boolean hasInstalledFilter = !TextUtils.isEmpty(currentVersion);
        boolean needToUpdateBuiltInFilter = !hasInstalledFilter
                || getVersionNumber(currentVersion) < getVersionNumber(BUILTIN_FILTER_VERSION);
        if (needToUpdateBuiltInFilter) {
            String builtInFilter = installBuiltInFilter();
            if (!TextUtils.isEmpty(builtInFilter)) {
                callback.call(builtInFilter);
                return;
            }
        }

        long now = System.currentTimeMillis();
        boolean isUpdateCheckTimeExpired = now >= getLastCheckTime() + UPDATE_INTERVAL;
        if (hasInstalledFilter && !isUpdateCheckTimeExpired) {
            Log.d(TAG, "load(): Skip checkUpdate()");
            VersionInfo.setFilterVersion(currentVersion);
            callback.call(new String());
            return;
        }

        // Delegate handling the callback to checkUpdate()
        checkUpdate(currentVersion, callback);
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<FilterLoader> {
        public Factory() {}

        @Override
        public FilterLoader createImpl() {
            return new FilterLoaderImpl();
        }
    }
}
