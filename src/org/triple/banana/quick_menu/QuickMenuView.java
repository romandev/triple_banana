// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;

interface QuickMenuView {
    interface Delegate {
        void onClickQuickMenuButton(@IdRes int buttonId);
    }

    void show();
    void dismiss();
    void setDelegate(Delegate delegate);
}
