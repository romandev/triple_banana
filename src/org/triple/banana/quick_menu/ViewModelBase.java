// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

abstract class ViewModelBase {
    interface Listener {
        void onUpdate(@NonNull ViewModelReadOnly data);
    }

    private final @NonNull Set<Listener> mListeners;

    ViewModelBase() {
        mListeners = new HashSet<>();
    }

    void addListener(@NonNull Listener listener) {
        mListeners.add(listener);
    }

    void removeListener(@Nullable Listener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    void notifyViews() {
        for (Listener listener : mListeners) {
            listener.onUpdate((ViewModelReadOnly) this);
        }
    }
}
