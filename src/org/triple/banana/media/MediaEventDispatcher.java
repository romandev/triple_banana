// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media;

import org.triple.banana.media.mojom.BananaMediaCommandProcessor;
import org.triple.banana.media.mojom.BananaMediaEventDispatcher;
import org.triple.banana.media.mojom.BananaPlayState;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

import java.util.HashSet;

public enum MediaEventDispatcher implements BananaMediaEventDispatcher {
    instance;

    private final HashSet<MediaEventListener> mListeners = new HashSet<>();

    public void addEventListener(MediaEventListener listener) {
        mListeners.add(listener);
    }

    public void removeEventListener(MediaEventListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onPlayStateChanged(int bananaState) {
        MediaPlayState state;
        switch (bananaState) {
            case BananaPlayState.PLAYING:
                state = MediaPlayState.PLAYING;
                break;
            case BananaPlayState.WAITING:
                state = MediaPlayState.WAITING;
                break;

            case BananaPlayState.PAUSED:
            default:
                state = MediaPlayState.PAUSED;
        }

        for (MediaEventListener listener : mListeners) {
            listener.onPlayStateChanged(state);
        }
    }

    @Override
    public void onTimeUpdate(double currentTime, double duration) {
        for (MediaEventListener listener : mListeners) {
            listener.onTimeUpdate(currentTime, duration);
        }
    }

    @Override
    public void onChangedPipMode(boolean value) {
        for (MediaEventListener listener : mListeners) {
            listener.onChangedPipMode(value);
        }
    }

    @Override
    public void onEnteredVideoFullscreen(BananaMediaCommandProcessor impl) {
        MediaCommandProcessor.instance.setImpl(impl);
        for (MediaEventListener listener : mListeners) {
            listener.onEnteredVideoFullscreen();
        }
    }

    @Override
    public void onExitedVideoFullscreen() {
        for (MediaEventListener listener : mListeners) {
            listener.onExitedVideoFullscreen();
        }
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<BananaMediaEventDispatcher> {
        public Factory() {}

        @Override
        public BananaMediaEventDispatcher createImpl() {
            return MediaEventDispatcher.instance;
        }
    }
}
