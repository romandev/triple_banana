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

class ViewControllerImpl implements ViewController, ButtonStateManager.Listener {
    private final @NonNull ViewModelImpl mViewModel;
    private final @NonNull ButtonInfoStorage mStorage;
    private final @NonNull ButtonActionProvider mActionProvider;
    private final @NonNull ButtonStateManager mStateManager;

    ViewControllerImpl(@NonNull ViewModelImpl viewModel, @NonNull ButtonInfoStorage storage,
            @NonNull ButtonActionProvider actionProvider) {
        mViewModel = viewModel;
        mStorage = storage;
        mActionProvider = actionProvider;
        mStateManager = ButtonStateManager.getInstance();
    }

    @Override
    public void onShow() {
        mViewModel.setButtons(mStorage.getButtons());
        mStateManager.addListener(this);
        mViewModel.notifyViews();
    }

    @Override
    public void onDismiss() {
        mStateManager.removeListener(this);
    }

    @Override
    public boolean onClickQuickMenuButton(@IdRes int buttonId) {
        mActionProvider.getAction(buttonId).run();
        return true /* dismiss */;
    }

    @Override
    public void onUpdateButtonState(@IdRes int id, @NonNull ButtonState state) {
        int image;
        int label;
        if (state == ButtonState.MOBILE_PAGE) {
            image = R.drawable.ic_phone_black_24dp;
            label = R.string.mobile_view;
        } else {
            image = R.drawable.ic_desktop_black_24dp;
            label = R.string.desktop_view;
        }
        mViewModel.updateButton(new ButtonInfo(id, image, label));
        mViewModel.notifyViews();
    }
}
