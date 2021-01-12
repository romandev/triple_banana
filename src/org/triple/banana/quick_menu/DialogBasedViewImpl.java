// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import org.triple.banana.R;

class DialogBasedViewImpl extends Dialog implements View {
    private @NonNull ViewEventController mController;

    DialogBasedViewImpl(@NonNull Context context, @NonNull ViewEventController controller) {
        super(context);

        mController = controller;

        setContentView(R.layout.quick_menu_layout);
    }

    @Override
    public void show() {
        // This calls Dialog.show()
        super.show();

        mController.onShow();
    }
}
