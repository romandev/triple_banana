// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

class QuickMenuStorageModelData {
    @IdRes
    int id;
    @IdRes
    int image;
    @StringRes
    int label;

    QuickMenuStorageModelData(@IdRes int id, @IdRes int image, @StringRes int label) {
        this.id = id;
        this.image = image;
        this.label = label;
    }
}