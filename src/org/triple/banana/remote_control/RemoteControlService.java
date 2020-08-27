// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.widget.Toast;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaPipController;
import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaController;
import org.triple.banana.media.MediaEventListener;
import org.triple.banana.media.MediaPlayState;
import org.triple.banana.util.AudioUtil;

public enum RemoteControlService implements RemoteControlView.Delegate {
    instance;

    private RemoteControlViewModel mViewModel = new RemoteControlViewModel();
    private RemoteControlViewImpl mView = new RemoteControlViewImpl(this);
    private boolean mWasPipMode;

    private MediaController mMediaController = MediaController.instance;

    public void start() {
        mViewModel.addListener(mView);
        mMediaController.addEventListener(new MediaEventListener() {
            @Override
            public void onPlayStateChanged(MediaPlayState state) {
                mViewModel.getEditor().setPlayState(state);
                mViewModel.commit();
            }

            @Override
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                mView.show(tab.getContext());
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
    public void onRemoteControlButtonClicked(int id) {
        if (id == R.id.play_button) {
            if (mViewModel.getEditor().getPlayState() == MediaPlayState.PAUSED) {
                mMediaController.play();
            } else {
                mMediaController.pause();
            }
        } else if (id == R.id.backward_button) {
            onBackward();
        } else if (id == R.id.forward_button) {
            onForward();
        } else if (id == R.id.brightness_up_button) {
            mViewModel.getEditor().setBrightness(1.0f);
            mViewModel.commit();
        } else if (id == R.id.brightness_down_button) {
            mViewModel.getEditor().setBrightness(0.2f);
            mViewModel.commit();
        } else if (id == R.id.volume_down_button) {
            mMediaController.setVolume(1.0f);
            AudioUtil.setMediaVolume(0.1f);
            mViewModel.getEditor().setVolume(0.1f);
            mViewModel.commit();
        } else if (id == R.id.volume_up_button) {
            mMediaController.setVolume(1.0f);
            AudioUtil.setMediaVolume(0.7f);
            mViewModel.getEditor().setVolume(0.7f);
            mViewModel.commit();
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
        } else if (id == R.id.close_button) {
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
    public void onPositionChanged(float value) {
        float position = mViewModel.getEditor().getPosition();
        mViewModel.getEditor().setControlsVisibility(true);
        mViewModel.getEditor().setPosition(position + value);
        mViewModel.commit();
        // FIXME(#589): Need to fix seekTo operation of MediaSession
        // mMediaController.setPosition(ms);
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
    }

    @Override
    public void onForward() {
        if (mViewModel.getData().getIsLocked()) return;

        mMediaController.setRelativePosition(10.0f);
    }
}
