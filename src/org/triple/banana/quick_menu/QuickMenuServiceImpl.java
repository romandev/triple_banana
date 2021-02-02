// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.button_state.ButtonStateManager;

import java.lang.ref.WeakReference;

enum QuickMenuServiceImpl implements QuickMenuService {
    instance;

    private @Nullable WeakReference<View> mView;

    @Override
    public void show(@NonNull Context context) {
        if (mView != null && mView.get() != null && mView.get().isShowing()) {
            // Prevent showing the quick menu duplicately
            return;
        }
        ViewModelImpl viewModel = new ViewModelImpl();
        ButtonInfoStorage storage = new ButtonInfoStorageImpl();
        ButtonActionProvider actionProvider = new ButtonActionProviderImpl();
        ButtonStateManager stateManager = ButtonStateManager.get();
        ViewController controller =
                new ViewControllerImpl(viewModel, storage, actionProvider, stateManager);
        View view = new DialogBasedViewImpl(context, controller);
        mView = new WeakReference(view);

        viewModel.addListener(new WeakReference(view));
        view.show();
    }
}
