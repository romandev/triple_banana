// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.adblock;

import android.app.AlarmManager;
import android.content.SharedPreferences;
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

public class FilterLoaderImpl implements FilterLoader {
    private static final String TAG = "FilterLoaderImpl";
    private final RemoteConfig mMetaData =
            new RemoteConfig("https://zino.dev/triple_banana_filter/metadata.json");

    private static final Uri FILTER_BASE_URL = Uri.parse("https://zino.dev/triple_banana_filter/");
    private static final String LAST_CHECK_TIME_KEY = "filter_download_last_check_time";
    private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_DAY * 3;

    private long getLastCheckTime() {
        return BananaApplicationUtils.get().getSharedPreferences().getLong(LAST_CHECK_TIME_KEY, 0);
    }

    private void updateLastCheckTime() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putLong(LAST_CHECK_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    @Override
    public void load(final String currentVersion, final LoadResponse callback) {
        long now = System.currentTimeMillis();
        if (now < getLastCheckTime() + UPDATE_INTERVAL) {
            VersionInfo.setFilterVersion(currentVersion);
            return;
        }

        mMetaData.getAsync(info -> {
            String newVersion = info.optString("version");
            Log.i(TAG, "load(): current = " + currentVersion + ", new = " + newVersion);
            if (TextUtils.equals(currentVersion, newVersion)) {
                VersionInfo.setFilterVersion(currentVersion);
                return;
            }

            final long filterSize = info.optLong("size");
            Uri filterUri = Uri.withAppendedPath(FILTER_BASE_URL, newVersion + ".filter.zip");
            SimpleDownloader.get().download(filterUri, compressedFilter -> {
                if (compressedFilter == null) {
                    Log.e(TAG, "load(): Downloading failed");
                    callback.call(new String());
                    return;
                }

                Log.i(TAG, "load(): Extract " + compressedFilter);
                if (!Unzip.get().extract(compressedFilter, true)) {
                    Log.e(TAG, "load(): Extracting failed");
                    callback.call(new String());
                    return;
                }

                File filterFile = new File(compressedFilter.getPath().replace(".zip", ""));
                if (filterFile.length() != filterSize) {
                    Log.e(TAG, "load(): Downloaded filter is corrupted");
                    callback.call(new String());
                    return;
                }

                compressedFilter.delete();
                // TODO(zino): How can we remove filterFile?

                VersionInfo.setFilterVersion(newVersion);

                Log.i(TAG, "load(): Filter updated");
                updateLastCheckTime();
                callback.call(filterFile.getPath());
            });
        });
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
