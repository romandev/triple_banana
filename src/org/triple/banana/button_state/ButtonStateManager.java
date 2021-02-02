// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.button_state;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public interface ButtonStateManager {
    enum ButtonState {
        ENABLE,
        DISABLE,
        DESKTOP_PAGE,
        MOBILE_PAGE,
    }

    static @NonNull ButtonStateManager get() {
        return new ButtonStateManagerImpl();
    }

    @NonNull
    ButtonState getButtonState(@IdRes int id);
}
