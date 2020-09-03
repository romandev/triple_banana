// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

class MediaRemoteViewImpl implements MediaRemoteView, MediaRemoteViewModel.Listener {
    private final @NonNull WeakReference<Delegate> mDelegate;
    private final @NonNull View.OnClickListener mClickListener;
    private final @NonNull MediaRemoteGestureDetector mMediaRemoteGestureDetector =
            new MediaRemoteGestureDetector();
    private @Nullable SeekBar mTimeSeekBar;
    private @Nullable Dialog mDialog;
    private @Nullable MediaRemoteLayout mMainView;
    private @Nullable WeakReference<Activity> mParentActivity;

    MediaRemoteViewImpl(MediaRemoteView.Delegate delegate) {
        mDelegate = new WeakReference<>(delegate);
        mClickListener = view -> {
            if (mDelegate.get() == null) return;
            mDelegate.get().onMediaRemoteButtonClicked(view.getId());
        };
    }

    @Override
    public void show(@NonNull Activity parentActivity) {
        if (parentActivity.isFinishing()) return;

        initializeDialogIfNeeded(parentActivity);
        hideSystemUI();
        mDialog.show();
        if (mMainView != null && mDelegate.get() != null) {
            mMediaRemoteGestureDetector.startDetection(mMainView, mDelegate.get());
        }
        mParentActivity = new WeakReference<>(parentActivity);
        if (parentActivity.getWindow() != null) {
            parentActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void dismiss() {
        if (mDialog == null) return;
        mDialog.dismiss();
        mMediaRemoteGestureDetector.stopDetection();

        if (mParentActivity == null) return;
        Activity activity = mParentActivity.get();
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mParentActivity.clear();
    }

    @Override
    public void showEffect(Effect effect) {
        View view;
        switch (effect) {
            case FORWARD:
                view = mDialog.findViewById(R.id.forward_effect);
                break;
            case BACKWARD:
                view = mDialog.findViewById(R.id.backward_effect);
                break;
            case NONE:
            default:
                view = null;
                break;
        }
        if (view != null) {
            view.setPressed(true);
            view.setPressed(false);
        }
    }

    @Override
    public void onUpdate(MediaRemoteViewModel.ReadonlyData data) {
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

        TextView timeTextView = mMainView.findViewById(R.id.time_text);
        if (timeTextView == null) return;
        timeTextView.setText(String.format("%s | %s", getTimeLabelFrom((long) currentTime), getTimeLabelFrom((long) duration)));
        mTimeSeekBar.setMax((int) duration);
        mTimeSeekBar.setProgress((int) currentTime);
    }

    private void updateVolumeUI(boolean visibility, float value) {
        if (mDialog == null) return;

        ViewGroup volumeContainer = mMainView.findViewById(R.id.volume_container);
        if (volumeContainer == null) return;
        volumeContainer.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);

        SeekBar volumeSeekBar = volumeContainer.findViewById(R.id.volume_seekbar);
        if (volumeSeekBar == null) return;
        volumeSeekBar.setProgress((int) (value * 100.0f));
    }

    private void updateBrightness(boolean visibility, float value) {
        if (mDialog == null) return;
        BrightnessUtil.setWindowBrightness(mDialog.getWindow(), value);

        ViewGroup brightnessContainer = mMainView.findViewById(R.id.brightness_container);
        if (brightnessContainer == null) return;
        brightnessContainer.setVisibility(visibility == true ? View.VISIBLE : View.INVISIBLE);

        SeekBar brightnessSeekBar = brightnessContainer.findViewById(R.id.brightness_seekbar);
        if (brightnessSeekBar == null) return;
        brightnessSeekBar.setProgress((int) (value * 100.0f));
    }

    private void hideSystemUI() {
        if (mDialog == null || !mDialog.isShowing()) return;
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

    private void showSystemUI() {
        if (mDialog == null || !mDialog.isShowing()) return;
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        mDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void showControls(boolean controlsVisibility, boolean isLocked, boolean isMuted) {
        if (mMainView == null) return;

        final ViewGroup controls = mMainView.findViewById(R.id.controls);
        final ImageButton lockButton = mMainView.findViewById(R.id.lock_button);
        final ImageButton muteButton = controls.findViewById(R.id.mute_button);
        if (controls == null || lockButton == null || muteButton == null) return;

        controls.setVisibility(controlsVisibility && !isLocked ? View.VISIBLE : View.INVISIBLE);
        lockButton.setVisibility(controlsVisibility ? View.VISIBLE : View.INVISIBLE);
        lockButton.setImageResource(isLocked ? R.drawable.ic_lock : R.drawable.ic_lock_opened);
        muteButton.setImageResource(isMuted ? R.drawable.ic_mute : R.drawable.ic_volume_up);

        if (controlsVisibility && !isLocked) {
            showSystemUI();
        } else {
            hideSystemUI();
        }
    }

    private void setPlayState(MediaPlayState state) {
        final ImageButton playButton = mMainView.findViewById(R.id.play_button);
        final ProgressBar waitingProgress = mMainView.findViewById(R.id.waiting_progress);

        if (playButton == null || waitingProgress == null) return;
        switch (state) {
            case PLAYING:
                waitingProgress.setVisibility(View.INVISIBLE);
                playButton.setImageResource(R.drawable.ic_pause);
                playButton.setVisibility(View.VISIBLE);
                break;
            case PAUSED:
                waitingProgress.setVisibility(View.INVISIBLE);
                playButton.setImageResource(R.drawable.ic_play);
                playButton.setVisibility(View.VISIBLE);
                break;
            case WAITING:
                waitingProgress.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void initializeDialogIfNeeded(@NonNull Activity parentActivity) {
        if (mDialog != null) return;
        mDialog = new Dialog(parentActivity, R.style.Theme_Banana_Fullscreen_Transparent_Dialog);
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (mDelegate.get() != null && keyCode == KeyEvent.KEYCODE_BACK) {
                mDelegate.get().onCancel();
                return true;
            }
            return false;
        });
        mDialog.create();
        mDialog.setContentView(R.layout.media_remote_view);

        mMainView = mDialog.findViewById(R.id.media_remote_view);
        mMainView.addListener(mDelegate.get());
        mTimeSeekBar = mDialog.findViewById(R.id.time_seekbar);
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
        mDialog.findViewById(R.id.rotate_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.lock_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.back_button).setOnClickListener(mClickListener);
        mDialog.findViewById(R.id.mute_button).setOnClickListener(mClickListener);

        // Pip mode button only visible on android 8.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDialog.findViewById(R.id.pip_button).setOnClickListener(mClickListener);
            mDialog.findViewById(R.id.pip_button).setVisibility(View.VISIBLE);
        }
    }
}
