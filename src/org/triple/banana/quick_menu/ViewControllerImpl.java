// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

class ViewControllerImpl implements ViewController {
    private final @NonNull ViewModelImpl mViewModel;
    private final @NonNull QuickMenuStorage mStorage;
    private final @NonNull QuickMenuActionProvider mActionProvider;

    ViewControllerImpl(@NonNull ViewModelImpl viewModel, @NonNull QuickMenuStorage storage,
            @NonNull QuickMenuActionProvider actionProvider) {
        mViewModel = viewModel;
        mStorage = storage;
        mActionProvider = actionProvider;
    }

    @Override
    public void onShow() {
        mViewModel.setButtons(mStorage.getButtons());
        mViewModel.notifyViews();
    }

    @Override
    public boolean onClickQuickMenuButton(@IdRes int buttonId) {
        mActionProvider.getAction(buttonId).run();
        return true /* dismiss */;
    }
}
