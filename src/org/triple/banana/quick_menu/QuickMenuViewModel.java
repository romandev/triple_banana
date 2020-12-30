// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.triple.banana.base.model.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class QuickMenuViewModel implements Model.Data {
    static class ButtonInfo implements Model.Data {
        @IdRes int id;
        @IdRes int image;
        @StringRes int label;

        ButtonInfo(@IdRes int id, @IdRes int image, @StringRes int label) {
            this.id = id;
            this.image = image;
            this.label = label;
        }
    }

    @NonNull Map<Integer, ButtonInfo> buttons = new LinkedHashMap<>();
    boolean isShowing;

    void addButton(@NonNull ButtonInfo info) {
        buttons.put(info.id, info);
    }

    @NonNull List<ButtonInfo> getButtons() {
        return new ArrayList<ButtonInfo>(buttons.values());
    }

    boolean getIsShowing() {
        return isShowing;
    }

    void setIsShowing(boolean showing) {
        isShowing = showing;
    }
}
