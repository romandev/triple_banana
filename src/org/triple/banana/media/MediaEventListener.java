// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media;

/**
 * Interface for notifying clients of the video event.
 */
public interface MediaEventListener {
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
