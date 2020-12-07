// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CaptionDataModel implements MediaRemoteOptionDialog.DataModel<CaptionDataModel.Data> {
    private final @NonNull Map<String, Integer> mIndexMap;
    private final @NonNull List<Data> mDataList;
    private @Nullable String[] mLabelCache;
    private @Nullable Integer mSelectedIndex;

    static class Data {
        final @NonNull String label;
        final @NonNull String languageCode;

        Data(@NonNull String label, @NonNull String languageCode) {
            this.label = label;
            this.languageCode = languageCode;
        }
    }

    CaptionDataModel(@NonNull JSONArray availableCaptionList, @NonNull JSONObject currentCaption) {
        mIndexMap = new HashMap<>();
        mDataList = new ArrayList<>();

        mDataList.add(new Data("Unused", ""));

        for (int i = 0; i < availableCaptionList.length(); i++) {
            try {
                JSONObject captionInfo = availableCaptionList.getJSONObject(i);
                String label = captionInfo.optString("displayName", "");
                String languageCode = captionInfo.optString("languageCode", "");

                mIndexMap.put(languageCode, mDataList.size());
                mDataList.add(new Data(label, languageCode));
            } catch (Exception e) {
                continue;
            }
        }

        mSelectedIndex = mIndexMap.get(currentCaption.optString("languageCode", ""));
    }

    @Override
    public @NonNull CharSequence[] getLabels() {
        if (mLabelCache == null) {
            mLabelCache = new String[mDataList.size()];
            for (int i = 0; i < mDataList.size(); i++) {
                mLabelCache[i] = mDataList.get(i).label;
            }
        }

        return mLabelCache;
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex == null ? 0 : mSelectedIndex;
    }

    @Override
    public @NonNull Data getSelectedData(int index) {
        return mDataList.get(index);
    }
}
