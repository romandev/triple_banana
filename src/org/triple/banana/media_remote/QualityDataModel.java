// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class QualityDataModel implements MediaRemoteOptionDialog.DataModel<QualityDataModel.Data> {
    private static final @NonNull Map<String, String> QUALITY_MAP = new HashMap<String, String>() {
        {
            put("auto", "auto");
            put("tiny", "144p");
            put("small", "240p");
            put("medium", "360p");
            put("large", "480p");
            put("hd720", "720p");
            put("hd1080", "1080p");
            put("hd1440", "1440p");
            put("hd2160", "2160p");
            put("hd2880", "2880p");
            put("highres", "4320p");
        }
    };
    private final @NonNull Map<String, Data> mDataMap;
    private @Nullable String[] mLabelCache;
    private int mSelectedIndex;

    static class Data {
        final @NonNull String quality;

        Data(@NonNull String quality) {
            this.quality = quality;
        }
    }

    QualityDataModel(
            @NonNull final JSONArray availableQualities, @NonNull final String preferredQuality) {
        final Map<String, Integer> indexMap = new HashMap<>();
        mDataMap = new LinkedHashMap<>();

        // Put the "auto" option as first element.
        mDataMap.put("auto", new Data("auto"));
        for (int i = 0; i < availableQualities.length(); i++) {
            try {
                final String quality = availableQualities.getString(i);
                final String label = getLabelFrom(quality);
                indexMap.put(quality, mDataMap.size());
                mDataMap.put(label, new Data(quality));
            } catch (Exception e) {
                continue;
            }
        }
        if (!preferredQuality.equals("auto") && indexMap.containsKey(preferredQuality)) {
            mSelectedIndex = indexMap.get(preferredQuality);
        }
    }

    @Override
    public @NonNull CharSequence[] getLabels() {
        return mDataMap.keySet().toArray(new String[mDataMap.size()]);
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public @NonNull Data getSelectedData(int index) {
        return mDataMap.get(getLabels()[index].toString());
    }

    private @NonNull String getLabelFrom(@NonNull String quality) {
        String label = QUALITY_MAP.get(quality);
        return label == null ? quality : label;
    }
}
