// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.R;
import org.triple.banana.button_state.ButtonStateManager;
import org.triple.banana.button_state.ButtonStateManager.ButtonState;

class ViewControllerImpl implements ViewController {
    private final @NonNull ViewModelImpl mViewModel;
    private final @NonNull ButtonInfoStorage mStorage;
    private final @NonNull ButtonActionProvider mActionProvider;
    private final @NonNull ButtonStateManager mStateManager;

    ViewControllerImpl(@NonNull ViewModelImpl viewModel, @NonNull ButtonInfoStorage storage,
            @NonNull ButtonActionProvider actionProvider,
            @NonNull ButtonStateManager stateManager) {
        mViewModel = viewModel;
        mStorage = storage;
        mActionProvider = actionProvider;
        mStateManager = stateManager;
    }

    @Override
    public void onShow() {
        mViewModel.setButtons(mStorage.getButtons());
        for (final ButtonInfo buttonInfo : mViewModel.getButtons()) {
            final ButtonState state = mStateManager.getButtonState(buttonInfo.id);
            updateButtonState(buttonInfo.id, state);
        }
        mViewModel.notifyViews();
    }

    @Override
    public void onDismiss() {
    }

    @Override
    public boolean onClickQuickMenuButton(@IdRes int buttonId) {
        mActionProvider.getAction(buttonId).run();
        return true /* dismiss */;
    }

    private void updateButtonState(final @IdRes int id, final @NonNull ButtonState state) {
        int image = 0;
        int label = 0;
        boolean enabled = true;
        switch (state) {
            case MOBILE_PAGE:
                image = R.drawable.ic_phone_black_24dp;
                label = R.string.mobile_view;
                break;
            case DESKTOP_PAGE:
                image = R.drawable.ic_desktop_black_24dp;
                label = R.string.desktop_view;
                break;
            case DISABLE:
                enabled = false;
                break;
            case ENABLE:
                // The enabled is already `true`. So, do nothing here.
                break;
        }
        mViewModel.updateButton(new ButtonInfo(id, image, label, enabled));
    }
}
