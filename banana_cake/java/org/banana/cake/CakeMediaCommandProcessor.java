// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaMediaCommandProcessor;

import org.chromium.content_public.browser.MediaSession;

class CakeMediaCommandProcessor implements BananaMediaCommandProcessor {
    private final @Nullable MediaSession mSession;

    CakeMediaCommandProcessor(MediaSession session) {
        mSession = session;
    }

    @Override
    public void play() {
        if (mSession == null) return;
        mSession.resume();
    }

    @Override
    public void pause() {
        if (mSession == null) return;
        mSession.suspend();
    }

    @Override
    public void setPosition(double seconds) {
        if (mSession == null) return;
        if (seconds < 0.0 || Double.isNaN(seconds)) seconds = 0.0;
        // NOTE: This only works if the website supports seekTo. There is no default behavior. Be
        // careful when using it in the client.
        mSession.seekTo((long) (seconds * 1000));
    }

    @Override
    public void setRelativePosition(double seconds) {
        if (mSession == null) return;
        // NOTE: The following seek() method doesn't allow zero. On the other hands, this
        // setRelativePosition() allows zero and it might take `0` as an input for some reasons. So,
        // in that case, we should change the number to 1 as a workaround.
        if (seconds == 0 || Double.isNaN(seconds)) seconds = 0.001;
        mSession.seek((long) (seconds * 1000.0));
    }
}
