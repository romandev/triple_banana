// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.view.Menu;
import android.widget.FrameLayout;

public interface BananaAppMenu {
    static BananaAppMenu get() {
        return BananaInterfaceProvider.get(BananaAppMenu.class);
    }

    void prepareMenu(Menu menu);
    void prepareMenuButton(FrameLayout menuButton);
}
