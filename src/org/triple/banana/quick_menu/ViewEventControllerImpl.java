// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

class ViewEventControllerImpl implements ViewEventController {
    private @NonNull ViewModelImpl mViewModel;

    ViewEventControllerImpl(@NonNull ViewModelImpl viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onShow() {
        mViewModel.notifyViews();
    }
}
