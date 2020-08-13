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
    public void setPosition(long ms) {
        if (mSession == null) return;
        mSession.seek(ms);
    }

    @Override
    public void setRelativePosition(long ms) {
        if (mSession == null) return;
        mSession.seekTo(ms);
    }
}
