// Copyright 2021 The Triple Banana Authors. All rights reserved.
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

class DialogBasedViewImpl extends Dialog implements View {
    private final @NonNull ViewController mController;
    private final @NonNull RotationManager mRotationManager;
    private final @NonNull RotationManager.Listener mOrientationChangedListener;
    private final @NonNull Handler mHandler;
    private final @NonNull android.view.View.OnClickListener mClickListener;
    private @Nullable GridLayout mMiddleButtonContainer;

    DialogBasedViewImpl(@NonNull Context context, @NonNull ViewController controller) {
        super(context);

        mController = controller;
        mRotationManager = new RotationManager();
        mOrientationChangedListener = createOrientationChangedListener();
        mHandler = new Handler();
        mClickListener = createClickListener();

        setContentView(R.layout.quick_menu_layout);
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

        mMiddleButtonContainer = findViewById(R.id.quick_menu_grid_layout);
        // FIXME(#995): The code below need to move to xml.
        findViewById(R.id.quick_menu_icon).setClipToOutline(true);
        setVersion();

        QuickMenuTopButton clearData = findViewById(R.id.clear_data);
        if (clearData != null) {
            clearData.setOnClickListener(mClickListener);
        }

        QuickMenuTopButton settings = findViewById(R.id.banana_extension_settings);
        if (settings != null) {
            settings.setOnClickListener(mClickListener);
        }
    }

    @Override
    public void onUpdate(ViewModelReadOnly data) {
        updateButtons(data.getButtons());
    }

    @Override
    public void show() {
        // This calls Dialog.show()
        super.show();

        mRotationManager.addListener(mOrientationChangedListener);
        mController.onShow();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        mRotationManager.removeListener(mOrientationChangedListener);
    }

    private void setVersion() {
        TextView versionTextView = findViewById(R.id.version_text_view);
        VersionInfo versionInfo = new VersionInfo();
        versionTextView.setText(versionInfo.getVersionName());
    }

    private void updateButtons(@NonNull List<ButtonInfo> buttons) {
        mMiddleButtonContainer.removeAllViews();

        for (final ButtonInfo info : buttons) {
            final QuickMenuMiddleButton button = new QuickMenuMiddleButton(getContext());
            button.setId(info.id);
            button.setIcon(info.image);
            button.setText(info.label);
            button.setOnClickListener(mClickListener);
            setColumnWeight(button);
            mMiddleButtonContainer.addView(button);
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
        if (mMiddleButtonContainer.getColumnCount() != columnCount) {
            final int viewsCount = mMiddleButtonContainer.getChildCount();
            for (int i = 0; i < viewsCount; i++) {
                setColumnWeight((QuickMenuMiddleButton) mMiddleButtonContainer.getChildAt(i));
            }
            mMiddleButtonContainer.setColumnCount(columnCount);
        }
    }

    private RotationManager.Listener createOrientationChangedListener() {
        return info -> {
            mHandler.post(() -> relayout(info.orientation));
        };
    }

    private android.view.View.OnClickListener createClickListener() {
        return view -> {
            boolean shouldBeDismissed = mController.onClickQuickMenuButton(view.getId());
            if (shouldBeDismissed) {
                dismiss();
            }
        };
    }
}
