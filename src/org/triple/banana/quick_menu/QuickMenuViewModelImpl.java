// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class QuickMenuViewModelImpl implements QuickMenuViewModel<QuickMenuViewModelImpl.Data> {
    private static final @NonNull String TAG = "QuickMenuViewModelImpl";
    private @NonNull Data mData;
    private final @NonNull Data mEditor;
    private final @NonNull Set<Listener<Data>> mListeners = new HashSet<>();

    static class Data implements Cloneable {
        static class ButtonInfo {
            @IdRes
            int id;
            @IdRes
            int image;
            @StringRes
            int label;

            ButtonInfo(@IdRes int id, @IdRes int image, @StringRes int label) {
                this.id = id;
                this.image = image;
                this.label = label;
            }
        }
        @NonNull
        Map<Integer, ButtonInfo> buttons = new LinkedHashMap<>();
        boolean isShowing;

        void addButton(@NonNull ButtonInfo info) {
            buttons.put(info.id, info);
        }

        @NonNull
        List<ButtonInfo> getButtons() {
            return new ArrayList<ButtonInfo>(buttons.values());
        }

        boolean getIsShowing() {
            return isShowing;
        }

        void setIsShowing(boolean showing) {
            isShowing = showing;
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

    QuickMenuViewModelImpl() {
        mData = new Data();
        mEditor = new Data();
    }

    @Override
    public @NonNull Data getData() {
        return mData;
    }

    @Override
    public @NonNull Data getEditor() {
        return mEditor;
    }

    @Override
    public void addListener(@NonNull Listener<Data> listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(@NonNull Listener<Data> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void commit() {
        mData = mEditor.cloneData();
        for (Listener<Data> listener : mListeners) {
            listener.onUpdate(mData);
        }
    }
}
