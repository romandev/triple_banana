// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.HashSet;
import java.util.Set;

class QuickMenuViewModelImpl implements QuickMenuViewModel<QuickMenuViewModelData> {
    private @NonNull QuickMenuViewModelData mData;
    private final @NonNull QuickMenuViewModelData mEditor;
    private final @NonNull Set<Listener<QuickMenuViewModelData>> mListeners = new HashSet<>();

    QuickMenuViewModelImpl() {
        mData = new QuickMenuViewModelData();
        mEditor = new QuickMenuViewModelData();
    }

    @Override
    public @NonNull QuickMenuViewModelData getData() {
        return mData;
    }

    @Override
    public @NonNull QuickMenuViewModelData getEditor() {
        return mEditor;
    }

    @Override
    public void addListener(@NonNull Listener<QuickMenuViewModelData> listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(@NonNull Listener<QuickMenuViewModelData> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void commit() {
        mData = mEditor.cloneData();
        for (Listener<QuickMenuViewModelData> listener : mListeners) {
            listener.onUpdate(mData);
        }
    }
}
