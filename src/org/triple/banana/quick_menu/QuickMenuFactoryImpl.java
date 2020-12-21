// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;

import androidx.annotation.NonNull;

class QuickMenuFactoryImpl implements QuickMenuFactory {
    @Override
    public @NonNull QuickMenuController create(@NonNull Context context) {
        MockQuickMenuViewImpl view = new MockQuickMenuViewImpl(context);
        QuickMenuViewModelImpl viewModel = new QuickMenuViewModelImpl();
        QuickMenuActionProvider actionProvider = new QuickMenuActionProviderImpl();
        QuickMenuControllerImpl controller = new QuickMenuControllerImpl(viewModel, actionProvider);

        view.setDelegate(controller::onClickQuickMenuButton);
        viewModel.addListener(view::onUpdate);

        return controller;
    }
}
