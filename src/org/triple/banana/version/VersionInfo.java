// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.version;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaVersionInfo;

public class VersionInfo implements BananaVersionInfo {
    private static final String TAG = "VersionInfo";
    private static final String UNKNOWN_VERSION = "Unknown";
    private static String sFilterVersion = UNKNOWN_VERSION;

    @Override
    public String getVersionName() {
        try {
            Context context = BananaApplicationUtils.get().getApplicationContext();
            if (context == null) return null;
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            Log.e(TAG, "getVersionName(): Failed " + e.toString());
        }
        return null;
    }

    public static void setFilterVersion(String version) {
        if (TextUtils.isEmpty(version)) {
            sFilterVersion = UNKNOWN_VERSION;
        } else {
            sFilterVersion = version;
        }
    }

    public static String getFilterVersion() {
        return sFilterVersion;
    }
}
