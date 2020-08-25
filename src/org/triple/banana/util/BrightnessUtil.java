// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.content.Context;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.banana.cake.interfaces.BananaApplicationUtils;

public class BrightnessUtil {
    private static final String TAG = "BrightnessUtil";

    public static float getSystemBrightness() {
        int brightness = 0;

        try {
            Context context = BananaApplicationUtils.get().getApplicationContext();
            brightness = System.getInt(
                    context.getContentResolver(), System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            Log.e(TAG, "getSystemBrightness(): " + e.toString());
        }

        return brightness / 255.0f;
    }

    public static float getWindowBrightness(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        return lp.screenBrightness;
    }

    public static void setWindowBrightness(Window window, float value) {
        if (value < 0.0f) {
            value = 0.0f;
        } else if (value > 1.0f) {
            value = 1.0f;
        }

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = value;
        window.setAttributes(lp);
    }
}
