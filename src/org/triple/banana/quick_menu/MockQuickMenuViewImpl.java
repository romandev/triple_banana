// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.R;
import org.triple.banana.quick_menu.QuickMenuViewModelData.ButtonInfo;

import java.util.List;

class MockQuickMenuViewImpl extends Dialog implements QuickMenuView, View.OnClickListener {
    private @Nullable Delegate mDelegate;

    MockQuickMenuViewImpl(@NonNull Context context) {
        super(context, R.style.Theme_Chromium_AlertDialog_NoActionBar);
        setContentView(R.layout.mock_quick_menu_view);
        setCanceledOnTouchOutside(false);
    }

    private void updateButtons(@NonNull List<ButtonInfo> buttons) {
        final GridLayout layout = (GridLayout) findViewById(R.id.mock_quick_menu_layout);
        layout.removeAllViews();
        for (final ButtonInfo info : buttons) {
            final Button button = new Button(getContext());
            button.setLayoutParams(
                    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            button.setId(info.id);
            button.setCompoundDrawablesWithIntrinsicBounds(info.image, 0, 0, 0);
            button.setText(info.label);
            button.setAllCaps(false);
            button.setOnClickListener(this);
            layout.addView(button);
        }
    }

    public void onUpdate(@NonNull QuickMenuViewModelData data) {
        if (data.getIsShowing()) {
            updateButtons(data.getButtons());
            show();
        } else {
            dismiss();
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
}
