// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.adblock;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.adblock.mojom.FilterLoader;
import org.triple.banana.download.SimpleDownloader;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class FilterLoaderImpl implements FilterLoader {
    private static final String TAG = "FilterLoaderImpl";

    // FIXME(zino): We should modify the following URL but now let's just use it for test.
    private static final String FILTER_URL = "https://www.bromite.org/filters/filters.dat";
    private static final String LAST_CHECK_TIME_KEY = "filter_download_last_check_time";
    private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_DAY * 14;

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
    public void load(final LoadResponse callback) {
        long now = System.currentTimeMillis();
        if (now < getLastCheckTime() + UPDATE_INTERVAL) return;

        // TODO(zino): We might have to consider meta check first because the filter data might be
        // large.

        // TODO(zino): We might have to consider zip compression.

        Log.i(TAG, "load(): Try to update filter");
        SimpleDownloader.get().download(Uri.parse(FILTER_URL), result -> {
            if (result == null) {
                callback.call(new String());
                return;
            }

            Log.i(TAG, "load(): Filter updated");
            updateLastCheckTime();
            callback.call(result.getAbsolutePath());
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
