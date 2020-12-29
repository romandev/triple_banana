// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.quick_menu.QuickMenuViewModelData.ButtonInfo;

class QuickMenuControllerImpl implements QuickMenuController, QuickMenuView.Delegate {
    private final @NonNull QuickMenuViewModel<QuickMenuViewModelData> mQuickMenuViewModel;
    private final @NonNull QuickMenuStorageModel<QuickMenuStorageModelData> mQuickMenuStorageModel;
    private final @NonNull QuickMenuActionProvider mQuickMenuActionProvider;

    QuickMenuControllerImpl(@NonNull QuickMenuViewModel<QuickMenuViewModelData> quickMenuViewModel,
            @NonNull QuickMenuStorageModel<QuickMenuStorageModelData> quickMenuStorageModel,
            @NonNull QuickMenuActionProvider quickMenuActionProvider) {
        mQuickMenuViewModel = quickMenuViewModel;
        mQuickMenuStorageModel = quickMenuStorageModel;
        mQuickMenuActionProvider = quickMenuActionProvider;
        loadButtons();
    }

    private void loadButtons() {
        for (QuickMenuStorageModelData data : mQuickMenuStorageModel.loadData()) {
            mQuickMenuViewModel.getEditor().addButton(
                    new ButtonInfo(data.id, data.image, data.label));
        }
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
