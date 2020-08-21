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
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                mView.show(tab.getContext());
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
            mMediaController.play();
        } else if (id == R.id.pause_button) {
            mMediaController.pause();
        } else if (id == R.id.backward_button) {
            mMediaController.setRelativePosition(-10000);
        } else if (id == R.id.forward_button) {
            mMediaController.setRelativePosition(10000);
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
            // NOTIMPLEMENTED
        } else if (id == R.id.time_seek_bar) {
            // NOTIMPLEMENTED
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
        }
    }

    @Override
    public void onVolumeChanged(float value) {
        mMediaController.setVolume(1.0f);
        float currentValue = AudioUtil.getMediaVolume();
        AudioUtil.setMediaVolume(currentValue + value);
        mViewModel.getEditor().setVolume(currentValue + value);
        mViewModel.commit();
    }

    @Override
    public void onBrightnessChanged(float value) {
        float currentValue = mViewModel.getEditor().getBrightness();
        mViewModel.getEditor().setBrightness(currentValue + value);
        mViewModel.commit();
    }

    @Override
    public void onPositionChanged(float value) {}

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
}
