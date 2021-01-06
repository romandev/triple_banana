// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.base.model.Model;

class QuickMenuFactoryImpl implements QuickMenuFactory {
    private static @Nullable QuickMenuController sController;

    @Override
    public @NonNull QuickMenuController create(@NonNull Context context) {
        if (sController != null) return sController;

        QuickMenuViewImpl view = new QuickMenuViewImpl(context);
        Model<QuickMenuViewModel> viewModel = new Model<>(QuickMenuViewModel::new);
        QuickMenuStorage storage = new QuickMenuStorageImpl();
        QuickMenuActionProvider actionProvider = new QuickMenuActionProviderImpl();
        QuickMenuControllerImpl controller =
                new QuickMenuControllerImpl(view, viewModel, storage, actionProvider);

        view.setDelegate(controller::onClickQuickMenuButton);
        viewModel.addListener(view::onUpdate);

        sController = controller;

        return sController;
    }
}
