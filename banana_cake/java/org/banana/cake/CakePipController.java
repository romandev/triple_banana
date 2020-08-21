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
import org.chromium.content_public.browser.WebContents;

class CakePipController implements BananaPipController {
    private final @Nullable PictureInPictureController mPipController;

    CakePipController() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mPipController = null;
            return;
        }
        mPipController = new PictureInPictureController();
    }

    private ChromeActivity getLastTrackedFocusedChromeActivity() {
        Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
        if (!(activity instanceof ChromeActivity)) return null;
        return (ChromeActivity) activity;
    }

    @Override
    public void attemptPictureInPictureForLastFocusedActivity() {
        if (mPipController == null) return;
        ChromeActivity activity = getLastTrackedFocusedChromeActivity();
        if (activity == null) return;
        mPipController.attemptPictureInPicture(activity);
    }

    @Override
    public boolean isPictureInPictureAllowedForFullscreenVideo() {
        if (mPipController == null) return false;
        ChromeActivity activity = getLastTrackedFocusedChromeActivity();
        if (activity == null || activity.getActivityTab() == null) return false;
        WebContents webContents = activity.getActivityTab().getWebContents();
        if (webContents == null) return false;
        return webContents.isPictureInPictureAllowedForFullscreenVideo();
    }
}
