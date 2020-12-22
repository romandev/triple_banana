// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.R;
import org.triple.banana.quick_menu.QuickMenuViewModelData.ButtonInfo;

class QuickMenuControllerImpl implements QuickMenuController, QuickMenuView.Delegate {
    private final @NonNull QuickMenuViewModel<QuickMenuViewModelData> mQuickMenuViewModel;
    private final @NonNull QuickMenuActionProvider mQuickMenuActionProvider;

    QuickMenuControllerImpl(@NonNull QuickMenuViewModel<QuickMenuViewModelData> quickMenuViewModel,
            @NonNull QuickMenuActionProvider quickMenuActionProvider) {
        mQuickMenuViewModel = quickMenuViewModel;
        mQuickMenuActionProvider = quickMenuActionProvider;
        loadButtons();
    }

    private void loadButtons() {
        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.dark_mode, R.drawable.ic_dark_black_24dp, R.string.dark_mode));
    }

    @Override
    public void onClickQuickMenuButton(@IdRes int buttonId) {
        mQuickMenuActionProvider.getAction(buttonId).doAction();
    }

    @Override
    public void show() {
        mQuickMenuViewModel.getEditor().setIsShowing(true);
        mQuickMenuViewModel.commit();
    }

    @Override
    public void dismiss() {
        mQuickMenuViewModel.getEditor().setIsShowing(false);
        mQuickMenuViewModel.commit();
    }
}
