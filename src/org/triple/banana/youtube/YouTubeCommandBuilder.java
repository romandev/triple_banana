// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.youtube;

import androidx.annotation.NonNull;

import org.json.JSONObject;

class YouTubeCommandBuilder {
    private static final @NonNull String PLAYER = "document.querySelector('.html5-video-player')";

    YouTubeCommandBuilder() {}

    @NonNull
    String getOption(@NonNull String module, @NonNull String option) {
        return String.format("%s.getOption('%s', '%s');", PLAYER, module, option);
    }

    @NonNull
    String setOption(@NonNull String module, @NonNull String option, @NonNull JSONObject value) {
        return String.format(
                "%s.setOption('%s', '%s', %s);", PLAYER, module, option, value.toString());
    }

    @NonNull
    String getPreferredQuality() {
        return String.format("%s.getPreferredQuality();", PLAYER);
    }

    @NonNull
    String getAvailableQualityLevels() {
        return String.format("%s.getAvailableQualityLevels();", PLAYER);
    }

    @NonNull
    String setPlaybackQualityRange(@NonNull String quality) {
        return String.format("%s.setPlaybackQualityRange('%s');", PLAYER, quality);
    }

    @NonNull
    String setPlaybackQuality(@NonNull String quality) {
        return String.format("%s.setPlaybackQuality('%s');", PLAYER, quality);
    }
}
