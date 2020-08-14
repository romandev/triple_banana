// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaPipController;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeActivity;
import org.chromium.chrome.browser.media.PictureInPictureController;

class CakePipController implements BananaPipController {
    private final @Nullable PictureInPictureController mPipController;

    CakePipController() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mPipController = null;
            return;
        }
        mPipController = new PictureInPictureController();
    }

    @Override
    public void attemptPictureInPictureForLastFocusedActivity() {
        if (mPipController == null) return;
        Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
        if (!(activity instanceof ChromeActivity)) return;
        mPipController.attemptPictureInPicture((ChromeActivity) activity);
    }
}
