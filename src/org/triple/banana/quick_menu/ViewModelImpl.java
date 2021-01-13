// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class ViewModelImpl extends ViewModelBase implements ViewModelReadOnly {
    private final @NonNull List<ButtonInfo> mButtons = new ArrayList<>();

    void setButtons(List<ButtonInfo> buttons) {
        mButtons.clear();
        for (ButtonInfo buttonInfo : buttons) {
            mButtons.add(buttonInfo);
        }
    }

    @Override
    public @NonNull List<ButtonInfo> getButtons() {
        return mButtons;
    }
}
