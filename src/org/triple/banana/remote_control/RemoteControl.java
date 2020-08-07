// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

/**
 * Interface for remote controlling of the video.
 */
interface RemoteControl {
    void play();
    void pause();
    void setPosition(float position);
    void setRelativePosition(float position);
    void setVolume(float value);
    void setRelativeVolume(float value);
}
