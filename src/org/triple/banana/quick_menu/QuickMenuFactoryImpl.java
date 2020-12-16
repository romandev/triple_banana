// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

class QuickMenuFactoryImpl implements QuickMenuFactory {
    @Override
    public QuickMenuController create() {
        QuickMenuViewImpl view = new QuickMenuViewImpl();
        QuickMenuViewModelImpl viewModel = new QuickMenuViewModelImpl();
        QuickMenuControllerImpl controller = new QuickMenuControllerImpl(viewModel);

        view.setDelegate(controller::onClickQuickMenuButton);
        viewModel.addListener(view::onUpdate);

        return controller;
    }
}