// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.youtube.YouTubePlayerController;

class YouTubeOptionDialogDelegate {
    private final @NonNull MediaRemoteOptionDialog mDialog;
    private final @NonNull YouTubePlayerController mYouTubeController;

    YouTubeOptionDialogDelegate() {
        mDialog = new MediaRemoteOptionDialog();
        mYouTubeController = new YouTubePlayerController();
    }

    void showCaptionDialog(final @Nullable Context context) {
        if (context == null) return;

        mYouTubeController.getAvailableCaptionList(availableCaptionList -> {
            mYouTubeController.getCurrentCaption(currentCaption -> {
                CaptionDataModel model = new CaptionDataModel(availableCaptionList, currentCaption);
                mDialog.show(context, model,
                        result -> { mYouTubeController.setCaption(result.languageCode); });
            });
        });
    }

    void dismiss() {
        mDialog.dismiss();
    }
}
