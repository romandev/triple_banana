// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;
import org.triple.banana.media.MediaController;
import org.triple.banana.media.MediaEventListener;

public enum RemoteControlService {
    instance;

    private MediaController mMediaController = MediaController.instance;

    public void start() {
        mMediaController.addEventListener(new MediaEventListener() {
            @Override
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                MockRemoteControlView view = new MockRemoteControlView(
                        tab.getContext(), R.style.Theme_Chromium_Activity_Fullscreen_Transparent);
                view.show();
            }
        });
    }

    public void play() {
        mMediaController.play();
    }

    public void pause() {
        mMediaController.pause();
    }

    public void setPosition(float position) {
        mMediaController.setPosition(position);
    }

    public void setRelativePosition(float position) {
        mMediaController.setRelativePosition(position);
    }

    public void setVolume(float value) {
        mMediaController.setVolume(value);
    }

    public void setRelativeVolume(float value) {
        mMediaController.setRelativeVolume(value);
    }
}
