// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.util.BrightnessUtil;

import java.lang.ref.WeakReference;

class RemoteControlViewImpl implements RemoteControlView, RemoteControlViewModel.Listener {
    private final @NonNull WeakReference<Delegate> mDelegate;
    private final @NonNull View.OnClickListener mClickListener;
    private final @NonNull RemoteControlGestureDetector mRemoteControlGestureDetector =
            new RemoteControlGestureDetector();
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
        hideSystemUI();
        mDialog.show();
        setBrightness(BrightnessUtil.getSystemBrightness(mDialog.getContext()));
    }

    @Override
    public void dismiss() {
        if (mDialog == null) return;
        mDialog.dismiss();
        mRemoteControlGestureDetector.stopDetection();
    }

    @Override
    public void onUpdate(RemoteControlViewModel.ReadonlyData data) {
        if (mDialog == null) return;
        setBrightness(data.getBrightness());
    }

    private void setBrightness(float value) {
        if (mDialog == null) return;
        BrightnessUtil.setWindowBrightness(mDialog.getWindow(), value);
    }

    private void hideSystemUI() {
        if (mDialog == null) return;
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDialog.getWindow().getDecorView().setSystemUiVisibility(flags);

        BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
        if (tab == null || tab.getContext() == null) return;
        final View contentView = tab.getContentView();
        if (contentView == null) return;
        contentView.setSystemUiVisibility(flags);
    }

    private void initializeDialogIfNeeded(@NonNull Context context) {
        if (mDialog != null) return;
        mDialog = new Dialog(context, R.style.Theme_Banana_Fullscreen_Transparent_Dialog);
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (mDelegate.get() != null && keyCode == KeyEvent.KEYCODE_BACK) {
                mDelegate.get().onCancel();
                return true;
            }
            return false;
        });
        mDialog.create();
        mDialog.setContentView(R.layout.remote_control_view);

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
        mDialog.findViewById(R.id.time_seek_bar).setOnClickListener(mClickListener);
        mRemoteControlGestureDetector.startDetection(
                mDialog.findViewById(R.id.remote_control_view), mDelegate.get());

        // Pip mode button only visible on android 8.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDialog.findViewById(R.id.pip_button).setOnClickListener(mClickListener);
            mDialog.findViewById(R.id.pip_button).setVisibility(View.VISIBLE);
        }
    }
}
