// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.R;
import org.triple.banana.util.BrightnessUtil;

import java.lang.ref.WeakReference;

class RemoteControlViewImpl implements RemoteControlView {
    private final @NonNull WeakReference<Delegate> mDelegate;
    private final @NonNull View.OnClickListener mClickListener;
    private @Nullable Dialog mDialog;

    RemoteControlViewImpl(RemoteControlView.Delegate delegate) {
        mDelegate = new WeakReference<>(delegate);
        mClickListener = view -> {
            if (mDelegate.get() == null) return;
            mDelegate.get().onRemoteControlButtonClicked(view.getId());
        };
    }

    @Override
    public void show(@NonNull Context context) {
        initializeDialogIfNeeded(context);
        mDialog.show();
        setBrightness(BrightnessUtil.getSystemBrightness(mDialog.getContext()));
    }

    @Override
    public void dismiss() {
        if (mDialog == null) return;
        mDialog.dismiss();
    }

    @Override
    public void setBrightness(float value) {
        if (mDialog == null) return;
        BrightnessUtil.setWindowBrightness(mDialog.getWindow(), value);
    }

    private void initializeDialogIfNeeded(@NonNull Context context) {
        if (mDialog != null) return;

        mDialog = new Dialog(context, R.style.Theme_Chromium_Activity_Fullscreen_Transparent);
        mDialog.create();
        mDialog.setContentView(R.layout.mock_remote_control_view);

        mDialog.findViewById(R.id.play_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.pause_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.backward_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.forward_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.brightness_up_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.brightness_down_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.volume_up_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.volume_down_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.rotate_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.lock_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.seek_bar).setOnClickListener(mClickListener);
    }
}
