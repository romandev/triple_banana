// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

abstract class ViewModelBase {
    interface Listener {
        void onUpdate(@NonNull ViewModelReadOnly data);
    }

    private final @NonNull Set<WeakReference<Listener>> mListeners;

    ViewModelBase() {
        mListeners = new HashSet<>();
    }

    void addListener(@NonNull WeakReference<Listener> listener) {
        mListeners.add(listener);
    }

    void removeListener(@NonNull WeakReference<Listener> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    void notifyViews() {
        for (WeakReference<Listener> listener : mListeners) {
            if (listener.get() == null) continue;
            listener.get().onUpdate((ViewModelReadOnly) this);
        }
    }
}
