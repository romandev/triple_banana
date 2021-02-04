// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.app.Activity;
import android.app.Dialog;
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

    DialogBasedViewImpl(@NonNull Activity activity, @NonNull ViewController controller) {
        super(activity);

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
        inflateMiddleLayout();
        // FIXME(#995): The code below need to move to xml.
        findViewById(R.id.quick_menu_icon).setClipToOutline(true);
        setVersion();

        bindClickListenerToTopButtons();
        bindClickListenerToBottomButtons();
    }

    private void bindClickListenerToTopButtons() {
        QuickMenuTopButton userInterface = findViewById(R.id.user_interface);
        if (userInterface != null) {
            userInterface.setOnClickListener(mClickListener);
        }

        QuickMenuTopButton settings = findViewById(R.id.banana_extension_settings);
        if (settings != null) {
            settings.setOnClickListener(mClickListener);
        }
    }

    private void bindClickListenerToBottomButtons() {
        QuickMenuBottomButton addSecretTabButton = findViewById(R.id.add_secret_tab);
        if (addSecretTabButton != null) {
            addSecretTabButton.setOnClickListener(mClickListener);
        }

        QuickMenuBottomButton terminateButton = findViewById(R.id.terminate);
        if (terminateButton != null) {
            terminateButton.setOnClickListener(mClickListener);
        }

        QuickMenuBottomButton addNewTabButton = findViewById(R.id.new_tab);
        if (addNewTabButton != null) {
            addNewTabButton.setOnClickListener(mClickListener);
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
        mController.onDismiss();
    }

    private void setVersion() {
        TextView versionTextView = findViewById(R.id.version_text_view);
        VersionInfo versionInfo = new VersionInfo();
        versionTextView.setText(versionInfo.getVersionName());
    }

    private void inflateMiddleLayout() {
        int viewsCount = new ButtonInfoStorageImpl().getButtons().size();
        for (int i = 0; i < viewsCount; i++) {
            final QuickMenuMiddleButton button = new QuickMenuMiddleButton(getContext());
            setColumnWeight(button);
            mMiddleButtonContainer.addView(button);
            button.setOnClickListener(mClickListener);
        }
    }

    private void updateButtons(@NonNull List<ButtonInfo> buttons) {
        int viewsCount = mMiddleButtonContainer.getChildCount();
        for (int i = 0; i < viewsCount; i++) {
            QuickMenuMiddleButton button =
                    (QuickMenuMiddleButton) mMiddleButtonContainer.getChildAt(i);
            ButtonInfo info = buttons.get(i);

            button.setId(info.id);
            button.setEnabled(info.enabled);
            button.setIcon(info.image);
            button.setText(info.label);
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
