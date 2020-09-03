// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.widget.Toast;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaPipController;
import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaController;
import org.triple.banana.media.MediaEventListener;
import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.AudioUtil;

public enum MediaRemoteService implements MediaRemoteView
.Delegate {
    instance;

    private MediaRemoteViewModel mViewModel = new MediaRemoteViewModel();
    private MediaRemoteViewImpl mView = new MediaRemoteViewImpl(this);
    private boolean mWasPipMode;
    private boolean mWasPlaying;
    private boolean mWasControlsVisible;

    private MediaController mMediaController = MediaController.instance;
    private final Runnable mTaskToHideControls = () -> {
        mViewModel.getEditor().setControlsVisibility(false);
        mViewModel.commit();
    };
    private final Runnable mTaskToRetrySeeking = () -> {
        mMediaController.setRelativePosition(0.0f);
    };
    private Handler mHandler = new Handler();

    public void start() {
        mViewModel.addListener(mView);
        mViewModel.addListener(data -> {
            if (data.getControlsVisibility() == mWasControlsVisible) return;
            if (data.getControlsVisibility()) {
                requestToHideControlsAfter5seconds();
            } else {
                resetTimerToHideControls();
            }
            mWasControlsVisible = data.getControlsVisibility();
        });
        mMediaController.addEventListener(new MediaEventListener() {
            @Override
            public void onPlayStateChanged(MediaPlayState state) {
                mHandler.removeCallbacks(mTaskToRetrySeeking);
                if (mViewModel.getData().getPlayState() == MediaPlayState.PLAYING
                        && state == MediaPlayState.WAITING) {
                    mHandler.postDelayed(mTaskToRetrySeeking, 1000);
                }
                mViewModel.getEditor().setPlayState(state);
                mViewModel.commit();
            }

            @Override
            public void onTimeUpdate(double currentTime, double duration) {
                mViewModel.getEditor().setCurrentTime(currentTime);
                mViewModel.getEditor().setDuration(duration);
                mViewModel.commit();
            }

            @Override
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                mView.show((Activity) tab.getContext());
                mViewModel.reset();
            }

            @Override
            public void onExitedVideoFullscreen() {
                mView.dismiss();
            }

            @Override
            public void onChangedPipMode(boolean value) {
                if (value) {
                    mView.dismiss();
                } else if (mWasPipMode) {
                    onEnteredVideoFullscreen();
                }
                mWasPipMode = value;
            }
        });
    }

    @Override
    public void onCancel() {
        BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
        if (tab == null || tab.getContext() == null) return;
        tab.exitFullscreen();
    }

    @Override
    public void onMediaRemoteButtonClicked(int id) {
        if (id == R.id.play_button) {
            if (mViewModel.getEditor().getPlayState() == MediaPlayState.PAUSED) {
                mMediaController.play();
            } else {
                mMediaController.pause();
            }
        } else if (id == R.id.backward_button) {
            mMediaController.setRelativePosition(-10.0f);
        } else if (id == R.id.forward_button) {
            mMediaController.setRelativePosition(10.0f);
        } else if (id == R.id.rotate_button) {
            toggleOrientation();
        } else if (id == R.id.lock_button) {
            boolean isLocked = mViewModel.getData().getIsLocked();
            mViewModel.getEditor().setIsLocked(!isLocked);
            mViewModel.commit();
        } else if (id == R.id.pip_button) {
            if (BananaPipController.get().isPictureInPictureAllowedForFullscreenVideo()) {
                BananaPipController.get().attemptPictureInPictureForLastFocusedActivity();
            } else {
                Toast.makeText(BananaApplicationUtils.get().getApplicationContext(),
                             R.string.pip_not_supported, Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (id == R.id.back_button) {
            onCancel();
        } else if (id == R.id.mute_button) {
            boolean isVolumeMuted = mViewModel.getData().getIsVolumeMuted();
            AudioUtil.setMediaVolumeMuted(!isVolumeMuted);
            mViewModel.getEditor().setIsVolumeMuted(!isVolumeMuted);
            mViewModel.commit();
        }
    }

    @Override
    public void onVolumeChanged(float value) {
        if (mViewModel.getData().getIsLocked()) return;

        if (value == 0.0f /* isFinished */) {
            mViewModel.getEditor().setVolumeControlVisibility(false);
            mViewModel.commit();
            return;
        }

        float currentValue = mViewModel.getEditor().getVolume();
        mMediaController.setVolume(1.0f);
        AudioUtil.setMediaVolume(currentValue + value);
        mViewModel.getEditor().setControlsVisibility(false);
        mViewModel.getEditor().setVolumeControlVisibility(true);
        mViewModel.getEditor().setVolume(currentValue + value);
        mViewModel.getEditor().setIsVolumeMuted(false);
        mViewModel.commit();
    }

    @Override
    public void onBrightnessChanged(float value) {
        if (mViewModel.getData().getIsLocked()) return;

        if (value == 0.0f /* isFinished */) {
            mViewModel.getEditor().setBrightnessControlVisibility(false);
            mViewModel.commit();
            return;
        }

        float currentValue = mViewModel.getEditor().getBrightness();
        mViewModel.getEditor().setControlsVisibility(false);
        mViewModel.getEditor().setBrightnessControlVisibility(true);
        mViewModel.getEditor().setBrightness(currentValue + value);
        mViewModel.commit();
    }

    @Override
    public void onPositionChangeStart() {
        if (mViewModel.getData().getIsLocked()) return;

        if (mViewModel.getData().getPlayState() != MediaPlayState.PAUSED) {
            mMediaController.pause();
            mWasPlaying = true;
        } else {
            mWasPlaying = false;
        }
        resetTimerToHideControls();
    }

    @Override
    public void onPositionChange(float diff) {
        if (mViewModel.getData().getIsLocked()) return;

        double diffTime = diff * mViewModel.getData().getDuration();
        double newTime = mViewModel.getData().getCurrentTime() + diffTime;
        if (newTime < 0.0) {
            newTime = 0.0;
        } else if (newTime > mViewModel.getData().getDuration()) {
            newTime = mViewModel.getData().getDuration();
        }
        mViewModel.getEditor().setControlsVisibility(true);
        mViewModel.getEditor().setCurrentTime(newTime);
        mViewModel.commit();
    }

    @Override
    public void onPositionChangeFinish() {
        if (mViewModel.getData().getIsLocked()) return;

        mMediaController.setPosition((float) mViewModel.getData().getCurrentTime());
        if (mWasPlaying) {
            mMediaController.play();
            mWasPlaying = false;
        }
        requestToHideControlsAfter5seconds();
    }

    private void toggleOrientation() {
        BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
        if (tab == null || tab.getContext() == null) return;
        Activity activity = (Activity) tab.getContext();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onControlsStateChanged() {
        boolean currentVisibility = mViewModel.getData().getControlsVisibility();
        mViewModel.getEditor().setControlsVisibility(!currentVisibility);
        mViewModel.commit();
    }

    @Override
    public void onBackward() {
        if (mViewModel.getData().getIsLocked()) return;
        mMediaController.setRelativePosition(-10.0f);
        mView.showEffect(MediaRemoteView.Effect.BACKWARD);
    }

    @Override
    public void onForward() {
        if (mViewModel.getData().getIsLocked()) return;
        mMediaController.setRelativePosition(10.0f);
        mView.showEffect(MediaRemoteView.Effect.FORWARD);
    }

    @Override
    public void onInterceptTouchEvent() {
        requestToHideControlsAfter5seconds();
    }

    private void resetTimerToHideControls() {
        mHandler.removeCallbacks(mTaskToHideControls);
    }

    private void requestToHideControlsAfter5seconds() {
        resetTimerToHideControls();
        mHandler.postDelayed(mTaskToHideControls, 5000);
    }
}
