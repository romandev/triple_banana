// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import androidx.annotation.NonNull;

class PlaybackRateDataModel
        implements MediaRemoteOptionDialog.DataModel<PlaybackRateDataModel.Data> {
    private static final String[] AVAILABLE_PLAYBACK_RATE = {
            "0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};
    private int mSelectedIndex;

    static class Data {
        final double rate;

        Data(double rate) {
            this.rate = rate;
        }
    }

    PlaybackRateDataModel(double currentRate) {
        // This is an adjustment to the currentRate. Since playback rate's padding is 0.25,
        // so we can get the index using the following expression.
        // eg) If currentPlayback == 2.0, then 7
        mSelectedIndex = (int) (currentRate * 4 - 1);
        mSelectedIndex = Math.min(Math.max(mSelectedIndex, 0), AVAILABLE_PLAYBACK_RATE.length - 1);
    }

    @Override
    public @NonNull CharSequence[] getLabels() {
        return AVAILABLE_PLAYBACK_RATE;
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public @NonNull Data getSelectedData(int index) {
        double rate = Double.parseDouble(AVAILABLE_PLAYBACK_RATE[index]);
        return new Data(rate);
    }
}
