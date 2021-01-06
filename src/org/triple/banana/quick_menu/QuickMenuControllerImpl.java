// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.base.model.Model;

class QuickMenuControllerImpl implements QuickMenuController, QuickMenuView.Delegate {
    private final @NonNull QuickMenuView mView;
    private final @NonNull Model<QuickMenuViewModel> mViewModel;
    private final @NonNull QuickMenuStorage mStorage;
    private final @NonNull QuickMenuActionProvider mQuickMenuActionProvider;

    QuickMenuControllerImpl(@NonNull QuickMenuView view, @NonNull Model<QuickMenuViewModel> viewModel,
            @NonNull QuickMenuStorage storage,
            @NonNull QuickMenuActionProvider quickMenuActionProvider) {
        mView = view;
        mViewModel = viewModel;
        mStorage = storage;
        mQuickMenuActionProvider = quickMenuActionProvider;
    }

    @Override
    public void onClickQuickMenuButton(@IdRes int buttonId) {
        mQuickMenuActionProvider.getAction(buttonId).run();
        dismiss();
    }

    @Override
    public void show() {
        mViewModel.data.buttons = mStorage.getButtons();
        mViewModel.data.isShowing = true;
        mViewModel.commit();
    }

    @Override
    public void dismiss() {
        mViewModel.data.isShowing = false;
        mViewModel.commit();
    }
}
