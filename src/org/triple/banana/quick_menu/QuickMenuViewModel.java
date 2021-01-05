// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

import org.triple.banana.base.model.Model;

import java.util.ArrayList;
import java.util.List;

class QuickMenuViewModel implements Model.Data {
    @NonNull List<ButtonInfo> buttons = new ArrayList<>();
    boolean isShowing;
}
