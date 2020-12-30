// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.R;

class QuickMenuViewImpl implements QuickMenuView, View.OnClickListener {
    private @Nullable Delegate mDelegate;
    private @Nullable QuickMenuDialog mQuickMenuDialog;

    QuickMenuViewImpl(@NonNull Context context) {
        createDialog(context);
    }

    public void onUpdate(@NonNull QuickMenuViewModel data) {
        if (data.getIsShowing()) {
            mQuickMenuDialog.show(data.getButtons(), this);
        } else {
            mQuickMenuDialog.dismiss();
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        if (mDelegate == null) {
            return;
        }
        mDelegate.onClickQuickMenuButton(view.getId());
    }

    @Override
    public void setDelegate(@NonNull Delegate delegate) {
        mDelegate = delegate;
    }

    private void createDialog(@NonNull Context context) {
        mQuickMenuDialog = new QuickMenuDialog(context, R.layout.quick_menu_layout);
    }
}
