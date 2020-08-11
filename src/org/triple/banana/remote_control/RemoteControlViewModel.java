// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.util.Log;

import java.util.HashSet;

class RemoteControlViewModel {
    private static final String TAG = "RemoteControlViewModel";
    static class ReadonlyData {
        protected float mBrightness;
        protected float mVolume;

        float getBrightness() {
            return mBrightness;
        }

        float getVolume() {
            return mVolume;
        }
    }

    static class Data extends ReadonlyData implements Cloneable {
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
}
