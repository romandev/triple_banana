// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media;

import org.triple.banana.media.mojom.BananaMediaCommandProcessor;

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

    public void setPosition(double seconds) {
        mCommandProcessor.setPosition(seconds);
    }

    public void setRelativePosition(double seconds) {
        mCommandProcessor.setRelativePosition(seconds);
    }

    public void download() {
        mCommandProcessor.download();
    }

    public void setPlaybackRate(double rate) {
        mCommandProcessor.setPlaybackRate(rate);
    }

    public void getPlaybackRate(
            final BananaMediaCommandProcessor.GetPlaybackRateResponse callback) {
        mCommandProcessor.getPlaybackRate(callback);
    }

    public void setVolume(float value) {
        mCommandProcessor.setVolume(value);
    }

    public void setRelativeVolume(float value) {
        mCommandProcessor.setRelativeVolume(value);
    }
}
