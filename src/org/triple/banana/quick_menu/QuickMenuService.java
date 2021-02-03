// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Activity;

import androidx.annotation.NonNull;

public interface QuickMenuService {
    static @NonNull QuickMenuService get() {
        return QuickMenuServiceImpl.instance;
    }

    void show(@NonNull Activity activity);
}
