// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ViewModelImpl extends ViewModelBase implements ViewModelReadOnly {
    private final @NonNull Map<Integer, ButtonInfo> mButtonMap = new HashMap<>();
    private final @NonNull List<ButtonInfo> mButtons = new ArrayList<>();

    void setButtons(List<ButtonInfo> buttons) {
        mButtons.clear();
        for (ButtonInfo buttonInfo : buttons) {
            mButtons.add(buttonInfo);
            mButtonMap.put(buttonInfo.id, buttonInfo);
        }
    }

    @Override
    public @NonNull List<ButtonInfo> getButtons() {
        return mButtons;
    }

    void updateButton(@NonNull ButtonInfo newInfo) {
        ButtonInfo existingInfo = mButtonMap.get(newInfo.id);
        if (existingInfo == null) {
            return;
        }
        existingInfo.image = newInfo.image;
        existingInfo.label = newInfo.label;
    }
}
