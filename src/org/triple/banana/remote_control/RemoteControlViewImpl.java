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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.BrightnessUtil;

import java.lang.ref.WeakReference;

class RemoteControlViewImpl implements RemoteControlView, RemoteControlViewModel.Listener {
    private final @NonNull WeakReference<Delegate> mDelegate;
    private final @NonNull View.OnClickListener mClickListener;
    private final @NonNull RemoteControlGestureDetector mRemoteControlGestureDetector =
            new RemoteControlGestureDetector();
    private @Nullable SeekBar mTimeSeekBar;
    private @Nullable Dialog mDialog;
    private @Nullable ViewGroup mMainView;

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
        if (mMainView != null && mDelegate.get() != null) {
            mRemoteControlGestureDetector.startDetection(mMainView, mDelegate.get());
        }
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
        updateBrightness(data.getBrightnessControlVisibility(), data.getBrightness());
        updateVolumeUI(data.getVolumeControlVisibility(), data.getVolume());
        updateTimeInfo(data.getCurrentTime(), data.getDuration());
        showControls(data.getControlsVisibility(), data.getIsLocked(), data.getIsVolumeMuted());
        setPlayState(data.getPlayState());
    }

    private String getTimeLabelFrom(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60));
        if (h > 0) return String.format("%d:%02d:%02d", h, m, s);
        return String.format("%d:%02d", m, s);
    }

    private void updateTimeInfo(double currentTime, double duration) {
        if (mDialog == null || mTimeSeekBar == null) return;

        TextView currentTimeView = mMainView.findViewById(R.id.current_time);
        TextView durationView = mMainView.findViewById(R.id.duration);
        if (currentTimeView == null || durationView == null) return;

        currentTimeView.setText(getTimeLabelFrom((long) currentTime));
        durationView.setText(getTimeLabelFrom((long) duration));
        mTimeSeekBar.setMax((int) duration);
        mTimeSeekBar.setProgress((int) currentTime);
    }

    private void updateVolumeUI(boolean visibility, float value) {
        if (mDialog == null) return;

        ViewGroup volumeLayout = mMainView.findViewById(R.id.volume_layout);
        if (volumeLayout == null) return;
        volumeLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);

        SeekBar volumeSeekBar = volumeLayout.findViewById(R.id.volume_seek_bar);
        if (volumeSeekBar == null) return;
        volumeSeekBar.setProgress((int) (value * 100.0f));
    }

    private void updateBrightness(boolean visibility, float value) {
        if (mDialog == null) return;
        BrightnessUtil.setWindowBrightness(mDialog.getWindow(), value);

        ViewGroup brightnessLayout = mMainView.findViewById(R.id.brightness_layout);
        if (brightnessLayout == null) return;
        brightnessLayout.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);

        SeekBar brightnessSeekBar = brightnessLayout.findViewById(R.id.brightness_seek_bar);
        if (brightnessSeekBar == null) return;
        brightnessSeekBar.setProgress((int) (value * 100.0f));
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

    private void showControls(boolean controlsVisibility, boolean isLocked, boolean isMuted) {
        if (mMainView == null) return;

        final ViewGroup controls = mMainView.findViewById(R.id.control);
        final ImageButton lockButton = mMainView.findViewById(R.id.lock_button);
        final ImageButton muteButton = mMainView.findViewById(R.id.mute_button);
        if (controls == null || lockButton == null || muteButton == null) return;

        controls.setVisibility(controlsVisibility && !isLocked ? View.VISIBLE : View.INVISIBLE);
        lockButton.setVisibility(controlsVisibility ? View.VISIBLE : View.INVISIBLE);
        lockButton.setImageResource(isLocked ? R.drawable.ic_lock : R.drawable.ic_lock_opened);
        muteButton.setImageResource(isMuted ? R.drawable.ic_mute : R.drawable.ic_volume_up);
    }

    private void setPlayState(MediaPlayState state) {
        final ViewGroup middleControl = mMainView.findViewById(R.id.middle_control);
        final ImageButton playButton = mMainView.findViewById(R.id.play_button);
        final ProgressBar waitingProgressBar = mMainView.findViewById(R.id.waiting_progress_bar);

        if (middleControl == null || playButton == null || waitingProgressBar == null) return;
        switch (state) {
            case PLAYING:
                middleControl.setVisibility(View.VISIBLE);
                waitingProgressBar.setVisibility(View.INVISIBLE);
                playButton.setImageResource(R.drawable.ic_pause);
                break;
            case PAUSED:
                middleControl.setVisibility(View.VISIBLE);
                waitingProgressBar.setVisibility(View.INVISIBLE);
                playButton.setImageResource(R.drawable.ic_play);
                break;
            case WAITING:
                middleControl.setVisibility(View.INVISIBLE);
                waitingProgressBar.setVisibility(View.VISIBLE);
                break;
        }
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

        mMainView = mDialog.findViewById(R.id.remote_control_view);
        mTimeSeekBar = mDialog.findViewById(R.id.time_seek_bar);
        mTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int mPreviousProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mDelegate.get() == null) return;
                mDelegate.get().onPositionChangeStart();
                mPreviousProgress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mDelegate.get() == null) return;
                mDelegate.get().onPositionChange(
                        (seekBar.getProgress() - mPreviousProgress) / (float) seekBar.getMax());
                mDelegate.get().onPositionChangeFinish();
            }
        });

        mDialog.findViewById(R.id.play_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.backward_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.forward_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.brightness_up_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.brightness_down_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.volume_up_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.volume_down_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.rotate_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.lock_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.close_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.mute_button).setOnClickListener(mClickListener);

        // Pip mode button only visible on android 8.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDialog.findViewById(R.id.pip_button).setOnClickListener(mClickListener);
            mDialog.findViewById(R.id.pip_button).setVisibility(View.VISIBLE);
        }
    }
}
