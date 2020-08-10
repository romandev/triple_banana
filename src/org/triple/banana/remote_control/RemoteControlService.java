// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaController;
import org.triple.banana.media.MediaEventListener;

public enum RemoteControlService implements RemoteControlView.Delegate {
    instance;

    private RemoteControlView mView = new RemoteControlViewImpl(this);

    private MediaController mMediaController = MediaController.instance;

    public void start() {
        mMediaController.addEventListener(new MediaEventListener() {
            @Override
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                mView.show(tab.getContext());
            }
        });
    }

    @Override
    public void onRemoteControlButtonClicked(int id) {
        if (id == R.id.play_button) {
            mMediaController.play();
        } else if (id == R.id.pause_button) {
            mMediaController.pause();
        } else if (id == R.id.backward_button) {
            mMediaController.setRelativePosition(-10.0f);
        } else if (id == R.id.forward_button) {
            mMediaController.setRelativePosition(10.0f);
        } else if (id == R.id.brightness_up_button) {
            mView.setBrightness(1.0f);
        } else if (id == R.id.brightness_down_button) {
            mView.setBrightness(0.2f);
        } else if (id == R.id.volume_down_button) {
            mMediaController.setRelativeVolume(-0.1f);
        } else if (id == R.id.volume_up_button) {
            mMediaController.setRelativeVolume(0.1f);
        } else if (id == R.id.rotate_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.lock_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.seek_bar) {
            // NOTIMPLEMENTED
        }
    }
}
