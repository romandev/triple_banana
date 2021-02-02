// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.button_state;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaButtonStateProvider;
import org.triple.banana.R;
import org.triple.banana.base.function.Supplier;

class ButtonStateManagerImpl implements ButtonStateManager {
    private final @NonNull BananaButtonStateProvider mStateProvider;

    ButtonStateManagerImpl() {
        mStateProvider = BananaButtonStateProvider.get();
    }

    @Override
    public @NonNull ButtonState getButtonState(@IdRes int id) {
        // Button ids below should be kept in alphabetical order.
        assert id == R.id.adblock || id == R.id.archive || id == R.id.dark_mode
                || id == R.id.desktop_view || id == R.id.download || id == R.id.find_in_page
                || id == R.id.media_feature || id == R.id.qr_code || id == R.id.secure_dns
                || id == R.id.share || id == R.id.translate || id == R.id.visit_history;

        if (id == R.id.download) {
            return getGeneralButtonState(mStateProvider::canUseDownloadPage);
        } else if (id == R.id.share) {
            return getGeneralButtonState(mStateProvider::canUseShare);
        } else if (id == R.id.add_to_home) {
            return getGeneralButtonState(mStateProvider::canUseAddToHome);
        } else if (id == R.id.translate) {
            return getGeneralButtonState(mStateProvider::canUseTranslate);
        } else if (id == R.id.find_in_page) {
            return getGeneralButtonState(mStateProvider::isWebContentAvailable);
        } else if (id == R.id.desktop_view) {
            return getDesktopPageButtonState();
        }

        return ButtonState.ENABLE;
    }

    private @NonNull ButtonState getGeneralButtonState(Supplier<Boolean> stateQuery) {
        return stateQuery.get() ? ButtonState.ENABLE : ButtonState.DISABLE;
    }

    private @NonNull ButtonState getDesktopPageButtonState() {
        if (!mStateProvider.isWebContentAvailable()) return ButtonState.DISABLE;
        return mStateProvider.isDesktopPage() ? ButtonState.MOBILE_PAGE : ButtonState.DESKTOP_PAGE;
    }
}
