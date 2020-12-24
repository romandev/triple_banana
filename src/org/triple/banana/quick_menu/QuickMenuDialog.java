// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import org.triple.banana.R;

class QuickMenuDialog extends Dialog {
    QuickMenuDialog(@NonNull Context context, int resId) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(resId);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.windowAnimations = R.style.AnimationPopupStyle;

            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onContentChanged() {
        setTopLayout();
        setBottomLayout();
    }

    // FIXME: Move below code to quick_menu_layout.xml(#962)
    private void setTopLayout() {
        QuickMenuButton titleButton = findViewById(R.id.title_button);
        titleButton.setImageVisible(false);
        titleButton.setLabel("Triple Banana Browser");
    }

    // FIXME: Move below code to quick_menu_layout.xml(#962)
    private void setBottomLayout() {
        QuickMenuButton clearButton = findViewById(R.id.clear_browsing_data_button);
        clearButton.setLabelVisible(false);
        clearButton.setImageResource(R.drawable.ic_clear_browsing_data);

        QuickMenuButton cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setLabelVisible(false);
        cancelButton.setImageResource(R.drawable.ic_download);

        QuickMenuButton terminateButton = findViewById(R.id.terminate_button);
        terminateButton.setLabelVisible(false);
        terminateButton.setImageResource(R.drawable.ic_power_off_black);
    }
}
