// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class QuickMenuViewImpl implements QuickMenuView {
    private @Nullable Delegate mDelegate;

    public void onUpdate(@NonNull QuickMenuViewModelImpl.Data data) {
        // NOTIMPLEMENTED        
    }

    @Override
    public void show() {
        // NOTIMPLEMENTED
    }

    @Override
    public void dismiss() {
        // NOTIMPLEMENTED
    }

    @Override
    public void setDelegate(@NonNull Delegate delegate) {
        mDelegate = delegate;
    }
}
