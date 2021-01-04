// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;

import androidx.annotation.NonNull;

import org.triple.banana.base.model.Model;

class QuickMenuFactoryImpl implements QuickMenuFactory {
    @Override
    public @NonNull QuickMenuController create(@NonNull Context context) {
        QuickMenuViewImpl view = new QuickMenuViewImpl(context);
        QuickMenuStorageModel storageModel = new QuickMenuStorageModelImpl();
        Model<QuickMenuViewModel> viewModel = new Model<>(QuickMenuViewModel::new);
        QuickMenuActionProvider actionProvider = new QuickMenuActionProviderImpl();
        QuickMenuControllerImpl controller =
                new QuickMenuControllerImpl(viewModel, storageModel, actionProvider);

        view.setDelegate(controller::onClickQuickMenuButton);
        viewModel.addListener(view::onUpdate);

        return controller;
    }
}
