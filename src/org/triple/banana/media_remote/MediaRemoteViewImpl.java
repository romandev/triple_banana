// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.BrightnessUtil;
import org.triple.banana.util.RotationManager;

import java.lang.ref.WeakReference;

class MediaRemoteViewImpl implements MediaRemoteView, MediaRemoteViewModel.Listener {
    private final @NonNull WeakReference<Delegate> mDelegate;
    private final @NonNull MediaRemoteGestureDetector mGestureDetector;
    private final @NonNull View.OnClickListener mButtonClickedListener;
    private final @NonNull DialogInterface.OnKeyListener mCancelListener;
    private final @NonNull SeekBar.OnSeekBarChangeListener mPositionChangeListener;
    private final @NonNull RotationManager.Listener mOrientationChangedListener;
    private final @NonNull RotationManager mRotationManager;
    private final @NonNull Handler mHandler;
    private @NonNull DialogViewSelector $ = new DialogViewSelector(null);

    private @Nullable Dialog mDialog;
    private @Nullable WeakReference<Activity> mParentActivity;
    private @Nullable WeakReference<MediaRemoteViewModel.ReadonlyData> mLastUpdatedData;

    private View.OnClickListener createButtonClickedListener() {
        return view -> {
            if (mDelegate.get() == null) return;
            mDelegate.get().onMediaRemoteButtonClicked(view.getId());
        };
    }

    private DialogInterface.OnKeyListener createCancelListener() {
        return (dialog, keyCode, event) -> {
            if (mDelegate.get() != null && keyCode == KeyEvent.KEYCODE_BACK) {
                mDelegate.get().onCancel();
                return true;
            }
            return false;
        };
    }

    private SeekBar.OnSeekBarChangeListener createPositionChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
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
        };
    }

    private RotationManager.Listener createOrientationChangedListener() {
        return info -> {
            mHandler.post(() -> {
                // NOTE: Although the rotation manager detects the rotation and orientation
                // information of the user device correctly, the activity configuration's
                // orientation might be updated late. It might cause breaking layout. So, this
                // hack postpones layouting the content view to the follow task.
                relayoutContentView();
                if (mLastUpdatedData != null && mLastUpdatedData.get() != null) {
                    onUpdate(mLastUpdatedData.get());
                }
            });
        };
    }

    MediaRemoteViewImpl(MediaRemoteView.Delegate delegate) {
        mDelegate = new WeakReference<>(delegate);
        mGestureDetector = new MediaRemoteGestureDetector();
        mButtonClickedListener = createButtonClickedListener();
        mCancelListener = createCancelListener();
        mPositionChangeListener = createPositionChangeListener();
        mOrientationChangedListener = createOrientationChangedListener();
        mRotationManager = new RotationManager();
        mHandler = new Handler();
    }

    private void createDialog(@NonNull Activity parentActivity) {
        if (mDialog != null) return;
        mDialog = new Dialog(parentActivity, R.style.Theme_Banana_Fullscreen_Transparent_Dialog);
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener(mCancelListener);
        mDialog.create();
        $ = new DialogViewSelector(mDialog);
    }

    private void setupParentActivity(@NonNull Activity parentActivity) {
        mParentActivity = new WeakReference<>(parentActivity);
        if (parentActivity.getWindow() != null) {
            parentActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void resetParentActivity() {
        if (mParentActivity == null || mParentActivity.get() == null) return;
        if (mParentActivity.get().getWindow() != null) {
            mParentActivity.get().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mParentActivity.clear();
    }

    // FIXME: We should make the following class more general and factor out it to util module
    private static class DialogViewSelector {
        private final @NonNull WeakReference<Dialog> mContainer;

        private DialogViewSelector(@Nullable Dialog container) {
            mContainer = new WeakReference<Dialog>(container);
        }

        private static interface Action<T extends View> { public void onAction(T view); }

        private <T extends View> void select(int resourceId, Action<T> action) {
            if (mContainer.get() == null) return;
            T view = mContainer.get().findViewById(resourceId);
            if (view != null) action.onAction(view);
        }
    }

    private void relayoutContentView() {
        if (mDialog == null || !mDialog.isShowing()) return;

        mDialog.setContentView(R.layout.media_remote_view);

        $.<MediaRemoteLayout>select(R.id.media_remote_view, v -> {
            v.addListener(mDelegate.get());
            mGestureDetector.startDetection(v, mDelegate.get());
        });

        $.<SeekBar>select(
                R.id.time_seekbar, v -> v.setOnSeekBarChangeListener(mPositionChangeListener));

        int[] buttons = new int[] {R.id.play_button, R.id.backward_button, R.id.forward_button,
                R.id.rotate_button, R.id.lock_button, R.id.back_button, R.id.mute_button};
        for (int button : buttons) {
            $.select(button, v -> v.setOnClickListener(mButtonClickedListener));
        }

        // PIP mode button is only visible on android 8.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            $.select(R.id.pip_button, v -> {
                v.setOnClickListener(mButtonClickedListener);
                v.setVisibility(View.VISIBLE);
            });
        }
    }

    private void resetContentViewInteractionListeners() {
        if (mDialog == null || !mDialog.isShowing()) return;

        mRotationManager.removeListener(mOrientationChangedListener);

        $.<MediaRemoteLayout>select(R.id.media_remote_view, v -> {
            v.removeListener(mDelegate.get());
            mGestureDetector.stopDetection();
        });

        $.<SeekBar>select(R.id.time_seekbar, v -> v.setOnSeekBarChangeListener(null));

        final @IdRes int[] buttons =
                new int[] {R.id.play_button, R.id.backward_button, R.id.forward_button,
                        R.id.rotate_button, R.id.lock_button, R.id.back_button, R.id.mute_button};
        for (@IdRes int button : buttons) {
            $.select(button, v -> v.setOnClickListener(null));
        }

        // PIP mode button is only visible on android 8.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            $.select(R.id.pip_button, v -> v.setOnClickListener(null));
        }
    }

    @Override
    public void show(@NonNull Activity parentActivity) {
        if (parentActivity.isFinishing()) return;

        createDialog(parentActivity);

        setupParentActivity(parentActivity);
        mDialog.show();
        mRotationManager.addListener(mOrientationChangedListener);
    }

    @Override
    public void dismiss() {
        if (mDialog == null || !mDialog.isShowing()) return;

        resetContentViewInteractionListeners();
        mDialog.dismiss();
        resetParentActivity();
    }

    @Override
    public void showEffect(Effect effect) {
        @IdRes
        final int effectId;
        switch (effect) {
            case FORWARD:
                effectId = R.id.forward_effect;
                break;
            case BACKWARD:
                effectId = R.id.backward_effect;
                break;
            case NONE:
            default:
                effectId = 0;
                break;
        }

        $.select(effectId, v -> {
            v.setPressed(true);
            v.setPressed(false);
        });
    }

    @Override
    public void onUpdate(MediaRemoteViewModel.ReadonlyData data) {
        mLastUpdatedData = new WeakReference<MediaRemoteViewModel.ReadonlyData>(data);

        if (mDialog == null || !mDialog.isShowing()) return;

        updateBrightness(data);
        updateVolume(data);
        updateControls(data);
        updatePlayState(data);
        updateTimeInfo(data);
    }

    private void setVisible(@NonNull View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateBrightness(MediaRemoteViewModel.ReadonlyData data) {
        assert mDialog != null && mDialog.isShowing();

        BrightnessUtil.setWindowBrightness(mDialog.getWindow(), data.getBrightness());

        $.select(R.id.brightness_container,
                v -> setVisible(v, data.getBrightnessControlVisibility()));
        $.<SeekBar>select(
                R.id.brightness_seekbar, v -> v.setProgress((int) (data.getBrightness() * 100.0f)));
    }

    private void updateVolume(MediaRemoteViewModel.ReadonlyData data) {
        assert mDialog != null && mDialog.isShowing();

        $.select(R.id.volume_container, v -> setVisible(v, data.getVolumeControlVisibility()));
        $.<SeekBar>select(
                R.id.volume_seekbar, v -> v.setProgress((int) (data.getVolume() * 100.0f)));
    }

    private String getTimeLabelFrom(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60));
        if (h > 0) return String.format("%d:%02d:%02d", h, m, s);
        return String.format("%d:%02d", m, s);
    }

    private void updateTimeInfo(MediaRemoteViewModel.ReadonlyData data) {
        assert mDialog != null && mDialog.isShowing();

        String currentTimeLabel = getTimeLabelFrom((long) data.getCurrentTime());
        String durationLabel = getTimeLabelFrom((long) data.getDuration());
        $.<TextView>select(R.id.time_text,
                v -> v.setText(String.format("%s | %s", currentTimeLabel, durationLabel)));
        $.<SeekBar>select(R.id.time_seekbar, v -> {
            v.setMax((int) data.getDuration());
            v.setProgress((int) data.getCurrentTime());
        });
    }

    private void updateControls(MediaRemoteViewModel.ReadonlyData data) {
        assert mDialog != null && mDialog.isShowing();

        // isControlVisible isLocked  Action
        //         O            O     Show background, lockButton / Hide controls and system UI
        //         O            X     Show background, lockButton, controls, and system UI
        //         X            O     Hide background, lockButton, controls, and system UI
        //         X            X     Hide background, lockButton, controls, and system UI
        $.select(R.id.controls_background,
                v -> setVisible(v, data.getControlsVisibility()));
        $.select(R.id.controls,
                v -> setVisible(v, data.getControlsVisibility() && !data.isLocked()));
        $.<ImageButton>select(R.id.lock_button, v -> {
            v.setImageResource(data.isLocked() ? R.drawable.ic_lock : R.drawable.ic_lock_opened);
            setVisible(v, data.getControlsVisibility());
        });
        $.<ImageButton>select(R.id.mute_button, v -> {
            v.setImageResource(data.isMuted() ? R.drawable.ic_mute : R.drawable.ic_volume_up);
        });

        if (data.getControlsVisibility() && !data.isLocked()) {
            showSystemUI();
        } else {
            hideSystemUI();
        }
    }

    private void showSystemUI() {
        assert mDialog != null && mDialog.isShowing();

        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        mDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void hideSystemUI() {
        assert mDialog != null && mDialog.isShowing();

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

    private void updatePlayState(MediaRemoteViewModel.ReadonlyData data) {
        assert mDialog != null && mDialog.isShowing();

        $.<ImageButton>select(R.id.play_button, v -> {
            v.setImageResource(data.getPlayState() == MediaPlayState.PLAYING ? R.drawable.ic_pause
                                                                             : R.drawable.ic_play);
            setVisible(v, data.getPlayState() != MediaPlayState.WAITING);
        });
        $.select(R.id.waiting_progress,
                v -> setVisible(v, data.getPlayState() == MediaPlayState.WAITING));
    }
}
