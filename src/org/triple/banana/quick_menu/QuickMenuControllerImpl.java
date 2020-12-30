// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.base.model.Model;
import org.triple.banana.quick_menu.QuickMenuViewModel.ButtonInfo;

class QuickMenuControllerImpl implements QuickMenuController, QuickMenuView.Delegate {
    private final @NonNull Model<QuickMenuViewModel> mViewModel;
    private final @NonNull QuickMenuStorageModel<QuickMenuStorageModelData> mQuickMenuStorageModel;
    private final @NonNull QuickMenuActionProvider mQuickMenuActionProvider;

    QuickMenuControllerImpl(@NonNull Model<QuickMenuViewModel> viewModel,
            @NonNull QuickMenuStorageModel<QuickMenuStorageModelData> quickMenuStorageModel,
            @NonNull QuickMenuActionProvider quickMenuActionProvider) {
        mViewModel = viewModel;
        mQuickMenuStorageModel = quickMenuStorageModel;
        mQuickMenuActionProvider = quickMenuActionProvider;
        loadButtons();
    }

    private void loadButtons() {
        for (QuickMenuStorageModelData data : mQuickMenuStorageModel.loadData()) {
            mViewModel.data.addButton(
                    new ButtonInfo(data.id, data.image, data.label));
        }
    }

    @Override
    public void onClickQuickMenuButton(@IdRes int buttonId) {
        mQuickMenuActionProvider.getAction(buttonId).doAction();
        dismiss();
    }

    @Override
    public void show() {
        mViewModel.data.setIsShowing(true);
        mViewModel.commit();
    }

    @Override
    public void dismiss() {
        mViewModel.data.setIsShowing(false);
        mViewModel.commit();
    }
}
