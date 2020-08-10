// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media;

public enum MediaController {
    instance;

    private MediaEventDispatcher mEventDispatcher = MediaEventDispatcher.instance;
    private MediaCommandProcessor mCommandProcessor = MediaCommandProcessor.instance;

    public void addEventListener(MediaEventListener listener) {
        mEventDispatcher.addEventListener(listener);
    }

    public void removeEventListener(MediaEventListener listener) {
        mEventDispatcher.removeEventListener(listener);
    }

    public void play() {
        mCommandProcessor.play();
    }

    public void pause() {
        mCommandProcessor.pause();
    }

    public void setPosition(float position) {
        mCommandProcessor.setPosition(position);
    }

    public void setRelativePosition(float position) {
        mCommandProcessor.setRelativePosition(position);
    }

    public void setVolume(float value) {
        mCommandProcessor.setVolume(value);
    }

    public void setRelativeVolume(float value) {
        mCommandProcessor.setRelativeVolume(value);
    }
}
