// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.R;

// FIXME: QuickMenuGridLayout need to be replaced by xml code on QuickMenuDialog(#961)
class QuickMenuGridLayout extends GridLayout {
    public QuickMenuGridLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context);

        initialize(context);
    }

    private void initialize(Context context) {
        setColumn(context);
        setUseDefaultMargins(true);

        // FIXME: Connect button with QuickMenuViewModelImpl.Data.
        for (int i = 0; i < 16; i++) {
            QuickMenuButton button = new QuickMenuButton(context);
            button.setLabel(Integer.toString(i));
            button.setImageResource(R.drawable.btn_done);
            button.setLayoutParams(getGridLayoutParams());
            addView(button);
        }
    }

    private void setColumn(Context context) {
        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE)
            setColumnCount(8);
        else if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT)
            setColumnCount(4);
    }

    private GridLayout.LayoutParams getGridLayoutParams() {
        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f));
        params.width = 0;

        return params;
    }
}
