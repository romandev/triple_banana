// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.util.Log;

import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.AudioUtil;
import org.triple.banana.util.BrightnessUtil;

import java.util.HashSet;

class RemoteControlViewModel {
    private static final String TAG = "RemoteControlViewModel";

    static class ReadonlyData {
        protected float mBrightness;
        protected float mVolume;
        protected float mPosition;
        protected boolean mIsVolumeMuted;
        protected boolean mControlsVisibility;
        protected boolean mIsLocked;
        protected boolean mVolumeControlVisibility;
        protected boolean mBrightnessControlVisibility;
        protected MediaPlayState mPlayState;

        float getBrightness() {
            return mBrightness;
        }

        float getVolume() {
            return mVolume;
        }

        float getPosition() {
            return mPosition;
        }

        boolean getIsVolumeMuted() {
            return mIsVolumeMuted;
        }

        boolean getControlsVisibility() {
            return mControlsVisibility;
        }

        boolean getIsLocked() {
            return mIsLocked;
        }

        boolean getVolumeControlVisibility() {
            return mVolumeControlVisibility;
        }

        boolean getBrightnessControlVisibility() {
            return mBrightnessControlVisibility;
        }

        MediaPlayState getPlayState() {
            return mPlayState;
        }
    }

    static class Data extends ReadonlyData implements Cloneable {
        void reset() {
            setBrightness(BrightnessUtil.getSystemBrightness());
            setVolume(AudioUtil.getMediaVolume());
            setControlsVisibility(true);
            setIsLocked(false);
            setPosition(0.0f);
            setIsVolumeMuted(AudioUtil.isMediaVolumeMuted());
        }

        void setBrightness(float brightness) {
            if (brightness < 0.0f) {
                brightness = 0.0f;
            } else if (brightness > 1.0f) {
                brightness = 1.0f;
            }
            mBrightness = brightness;
        }

        void setVolume(float volume) {
            if (volume < 0.0f) {
                volume = 0.0f;
            } else if (volume > 1.0f) {
                volume = 1.0f;
            }
            mVolume = volume;
        }

        void setPosition(float position) {
            if (position < 0.0f) {
                position = 0.0f;
            } else if (position > 1.0f) {
                position = 1.0f;
            }
            mPosition = position;
        }

        void setIsVolumeMuted(boolean isVolumeMuted) {
            mIsVolumeMuted = isVolumeMuted;
        }

        void setControlsVisibility(boolean visibility) {
            mControlsVisibility = visibility;
        }

        void setIsLocked(boolean isLocked) {
            mIsLocked = isLocked;
        }

        void setVolumeControlVisibility(boolean visibility) {
            mVolumeControlVisibility = visibility;
        }

        void setBrightnessControlVisibility(boolean visibility) {
            mBrightnessControlVisibility = visibility;
        }

        void setPlayState(MediaPlayState state) {
            mPlayState = state;
        }

        Data cloneData() {
            try {
                Data clone = (Data) super.clone();
                return clone;
            } catch (Exception e) {
                Log.e(TAG, "cloneData(): " + e.toString());
            }
            return new Data();
        }
    }

    static interface Listener { void onUpdate(ReadonlyData data); }

    private final HashSet<Listener> mListeners = new HashSet<>();
    private final Data mEditor = new Data();
    private Data mData = new Data();

    Data getEditor() {
        return mEditor;
    }

    ReadonlyData getData() {
        return mData;
    }

    void addListener(Listener listener) {
        mListeners.add(listener);
    }

    void removeListener(Listener listener) {
        if (mListeners.contains(listener)) mListeners.remove(listener);
    }

    void commit() {
        mData = mEditor.cloneData();

        for (Listener listener : mListeners) {
            listener.onUpdate(mData);
        }
    }

    void reset() {
        mEditor.reset();
        commit();
    }
}
