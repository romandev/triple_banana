// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.R;
import org.triple.banana.quick_menu.ButtonInfo;
import org.triple.banana.util.RotationManager;
import org.triple.banana.version.VersionInfo;

import java.util.List;

class QuickMenuDialog extends Dialog {
    private @Nullable GridLayout mMiddleGridLayout;
    private final @NonNull RotationManager mRotationManager;
    private final @NonNull RotationManager.Listener mOrientationChangedListener;
    private final @NonNull Handler mHandler;

    QuickMenuDialog(@NonNull Context context, int resId) {
        super(context);

        mRotationManager = new RotationManager();
        mOrientationChangedListener = createOrientationChangedListener();
        mHandler = new Handler();

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

        mMiddleGridLayout = findViewById(R.id.quick_menu_grid_layout);
        // FIXME(#995): The code below need to move to xml.
        findViewById(R.id.quick_menu_icon).setClipToOutline(true);
        setVersion();
    }

    private void setVersion() {
        TextView versionTextView = findViewById(R.id.version_text_view);
        VersionInfo versionInfo = new VersionInfo();
        versionTextView.setText(versionInfo.getVersionName());
    }

    private void updateButtons(@NonNull List<ButtonInfo> buttons, @NonNull View.OnClickListener listener) {
        mMiddleGridLayout.removeAllViews();

        for (final ButtonInfo info : buttons) {
            final QuickMenuMiddleButton button = new QuickMenuMiddleButton(getContext());
            button.setId(info.id);
            button.setIcon(info.image);
            button.setText(info.label);
            button.setOnClickListener(listener);
            setColumnWeight(button);
            mMiddleGridLayout.addView(button);
        }
    }

    private void setColumnWeight(@NonNull QuickMenuMiddleButton button) {
        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f));
        params.width = 0;

        button.setLayoutParams(params);
    }

    private void relayout(RotationManager.Orientation orientation) {
        int columnCount = (orientation == RotationManager.Orientation.LANDSCAPE) ? 8 : 4;
        if (mMiddleGridLayout.getColumnCount() != columnCount) {
            final int viewsCount = mMiddleGridLayout.getChildCount();
            for (int i = 0; i < viewsCount; i++) {
                setColumnWeight((QuickMenuMiddleButton) mMiddleGridLayout.getChildAt(i));
            }
            mMiddleGridLayout.setColumnCount(columnCount);
        }
    }

    public void show(@NonNull List<ButtonInfo> buttons, @NonNull View.OnClickListener listener) {
        updateButtons(buttons, listener);
        show();
    }

    @Override
    public void show() {
        super.show();

        mRotationManager.addListener(mOrientationChangedListener);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        mRotationManager.removeListener(mOrientationChangedListener);
    }

    private RotationManager.Listener createOrientationChangedListener() {
        return info -> {
            mHandler.post(() -> relayout(info.orientation));
        };
    }
}
