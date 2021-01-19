// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.button_state;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaButtonStateManager;
import org.banana.cake.interfaces.BananaButtonStateProvider;
import org.triple.banana.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ButtonStateManager implements BananaButtonStateManager {
    public interface Listener {
        void onUpdateButtonState(@IdRes int id, @NonNull ButtonState state);
    }

    public enum ButtonState {
        ENABLE,
        DISABLE,
        DESKTOP_PAGE,
        MOBILE_PAGE,
    }

    private static @Nullable ButtonStateManager sInstance;
    private final @NonNull Set<Listener> mListeners = new HashSet<>();
    private final @NonNull Map<Integer, ButtonState> mCachedState = new HashMap<>();

    private ButtonStateManager() {}

    public static @NonNull ButtonStateManager getInstance() {
        if (sInstance == null) {
            sInstance = new ButtonStateManager();
        }
        return sInstance;
    }

    public void addListener(@NonNull Listener listener) {
        mListeners.add(listener);
        updateButtonStates();
        for (final Map.Entry<Integer, ButtonState> state : mCachedState.entrySet()) {
            notifyListeners(state.getKey(), state.getValue());
        }
    }

    public void removeListener(@NonNull Listener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void updateDesktopPageButtonState(boolean isDesktopPage) {
        final ButtonState state =
                isDesktopPage ? ButtonState.MOBILE_PAGE : ButtonState.DESKTOP_PAGE;
        mCachedState.put(R.id.desktop_view, state);
        notifyListeners(R.id.desktop_view, state);
    }

    private void notifyListeners(@IdRes int id, @NonNull ButtonState state) {
        for (final Listener listener : mListeners) {
            listener.onUpdateButtonState(id, state);
        }
    }

    private void updateButtonStates() {
        mCachedState.put(R.id.download,
                BananaButtonStateProvider.get().canUseDownloadPage() ? ButtonState.ENABLE
                                                                     : ButtonState.DISABLE);
        mCachedState.put(R.id.find_in_page,
                BananaButtonStateProvider.get().canUseFindInPage() ? ButtonState.ENABLE
                                                                   : ButtonState.DISABLE);
        mCachedState.put(R.id.share,
                BananaButtonStateProvider.get().canUseShare() ? ButtonState.ENABLE
                                                              : ButtonState.DISABLE);
        mCachedState.put(R.id.add_to_home,
                BananaButtonStateProvider.get().canUseAddToHome() ? ButtonState.ENABLE
                                                                  : ButtonState.DISABLE);
    }
}
