// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.content.Context;
import android.media.AudioManager;

import org.banana.cake.interfaces.BananaApplicationUtils;

public class AudioUtil {
    private static AudioManager getAudioManager() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static int getMediaVolume() {
        AudioManager am = getAudioManager();
        if (am == null) return 0;
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setMediaVolume(float volume) {
        AudioManager am = getAudioManager();
        if (am == null) return;
        if (volume < 0.0f) {
            volume = 0.0f;
        } else if (volume > 1.0f) {
            volume = 1.0f;
        }
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume * volume), 0 /* flag */);
    }
}
