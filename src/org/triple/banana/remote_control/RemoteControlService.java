// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import org.triple.banana.remote_control.mojom.BananaRemoteControlEventDispatcher;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

import java.util.ArrayList;
import java.util.List;

public class RemoteControlService implements BananaRemoteControlEventDispatcher, RemoteControl {
    // The listeners used to notify the clients.
    private final List<EventListener> mEventListeners = new ArrayList<EventListener>();

    /**
     * Interface for notifying clients of the video event.
     */
    public interface EventListener {
        /**
         * Called when entered or exited the pip mode.
         */
        default void onChangedPipMode(boolean value) {}

        /**
         * Called when entered the video fullscreen.
         */
        default void onEnteredVideoFullscreen() {}

        /**
         * Called when exited the video fullscreen.
         */
        default void onExitedVideoFullscreen() {}
    }

    public void addEventListener(EventListener listener) {
        mEventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        mEventListeners.remove(listener);
    }

    @Override
    public void onChangedPipMode(boolean value) {
        for (EventListener listener : mEventListeners) {
            listener.onChangedPipMode(value);
        }
    }

    @Override
    public void onEnteredVideoFullscreen() {
        for (EventListener listener : mEventListeners) {
            listener.onEnteredVideoFullscreen();
        }
    }

    @Override
    public void onExitedVideoFullscreen() {
        for (EventListener listener : mEventListeners) {
            listener.onExitedVideoFullscreen();
        }
    }

    @Override
    public void play() {
        // NOTIMPLEMENTED
    }

    @Override
    public void pause() {
        // NOTIMPLEMENTED
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<BananaRemoteControlEventDispatcher> {
        public Factory() {}

        @Override
        public BananaRemoteControlEventDispatcher createImpl() {
            return new RemoteControlService();
        }
    }
}
