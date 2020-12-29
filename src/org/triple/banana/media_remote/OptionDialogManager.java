// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.media.MediaController;
import org.triple.banana.youtube.YouTubePlayerController;

class OptionDialogManager {
    private final @NonNull MediaRemoteOptionDialog mDialog;
    private final @NonNull YouTubePlayerController mYouTubeController;
    private final @NonNull MediaController mMediaController = MediaController.instance;

    OptionDialogManager() {
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

    void showPlaybackRateDialog(final @Nullable Context context) {
        if (context == null) return;

        mMediaController.getPlaybackRate(rate -> {
            PlaybackRateDataModel model = new PlaybackRateDataModel(rate);
            mDialog.show(context, model, result -> mMediaController.setPlaybackRate(result.rate));
        });
    }

    void showQualityDialog(final @Nullable Context context) {
        if (context == null) return;
        mYouTubeController.getAvailableQualityLevels(availableQualites -> {
            mYouTubeController.getPreferredQuality(preferredQuality -> {
                QualityDataModel model = new QualityDataModel(availableQualites, preferredQuality);
                mDialog.show(
                        context, model, result -> mYouTubeController.setQuality(result.quality));
            });
        });
    }

    void dismiss() {
        mDialog.dismiss();
    }
}
