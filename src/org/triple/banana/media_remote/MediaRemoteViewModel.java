// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.util.Log;

import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.AudioUtil;
import org.triple.banana.util.BrightnessUtil;

import java.util.HashSet;

class MediaRemoteViewModel {
    private static final String TAG = "MediaRemoteViewModel";

    static class ReadonlyData {
        protected float mBrightness;
        protected float mVolume;
        protected double mCurrentTime;
        protected double mDuration;
        protected boolean mControlsVisibility;
        protected boolean mVolumeControlVisibility;
        protected boolean mBrightnessControlVisibility;
        protected boolean mIsLocked;
        protected boolean mIsMuted;
        protected MediaPlayState mPlayState = MediaPlayState.PAUSED;

        float getBrightness() {
            return mBrightness;
        }

        float getVolume() {
            return mVolume;
        }

        double getCurrentTime() {
            return mCurrentTime;
        }

        double getDuration() {
            return mDuration;
        }

        boolean getControlsVisibility() {
            return mControlsVisibility;
        }

        boolean getVolumeControlVisibility() {
            return mVolumeControlVisibility;
        }

        boolean getBrightnessControlVisibility() {
            return mBrightnessControlVisibility;
        }

        boolean isLocked() {
            return mIsLocked;
        }

        boolean isMuted() {
            return mIsMuted;
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
            setIsMuted(AudioUtil.isMediaVolumeMuted());
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

        void setCurrentTime(double currentTime) {
            mCurrentTime = currentTime;
        }

        void setDuration(double duration) {
            mDuration = duration;
        }

        void setControlsVisibility(boolean visibility) {
            mControlsVisibility = visibility;
        }

        void setVolumeControlVisibility(boolean visibility) {
            mVolumeControlVisibility = visibility;
        }

        void setBrightnessControlVisibility(boolean visibility) {
            mBrightnessControlVisibility = visibility;
        }

        void setIsLocked(boolean isLocked) {
            mIsLocked = isLocked;
        }

        void setIsMuted(boolean isMuted) {
            mIsMuted = isMuted;
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
