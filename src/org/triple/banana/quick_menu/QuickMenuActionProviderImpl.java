// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.triple.banana.R;
import org.triple.banana.theme.DarkModeController;

import java.util.HashMap;
import java.util.Map;

class QuickMenuActionProviderImpl implements QuickMenuActionProvider {
    final private @NonNull Map<Integer, Action> mActionMap;

    QuickMenuActionProviderImpl() {
        mActionMap = new HashMap<Integer, Action>() {
            { put(R.id.dark_mode, () -> DarkModeController.get().toggle()); }
        };
    }

    @Override
    public @NonNull Action getAction(@IdRes int id) {
        if (mActionMap.containsKey(id)) {
            return mActionMap.get(id);
        }
        return () -> {};
    }
}
