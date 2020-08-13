// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.content.Context;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaMediaCommandProcessor;
import org.banana.cake.interfaces.BananaTab;

import org.chromium.chrome.browser.tab.Tab;
import org.chromium.content_public.browser.MediaSession;
import org.chromium.url.GURL;

class CakeTab implements BananaTab {
    private Tab mTab;

    @Override
    public BananaTab wrap(Tab tab) {
        mTab = tab;
        return this;
    }

    /**
     * @return The URL that is currently visible in the location bar. This may not be the same as
     *         the last committed URL if a new navigation is in progress.
     */
    @Override
    public String getUrl() {
        if (mTab == null) return new String();
        return mTab.getUrl().getSpec();
    }

    /**
     * Injects the passed Javascript code in the current page and evaluates it.
     * If a result is required, pass in a callback.
     *
     * @param script The Javascript to execute.
     */
    @Override
    public void evaluateJavaScript(String script) {
        if (mTab == null) return;
        mTab.getWebContents().evaluateJavaScript(script, null);
    }

    @Override
    public Context getContext() {
        if (mTab == null) return null;
        return mTab.getContext();
    }

    @Override
    public void exitFullscreen() {
        if (mTab == null || mTab.getWebContents() == null) return;
        mTab.getWebContents().exitFullscreen();
    }

    @Override
    public @NonNull BananaMediaCommandProcessor getMediaCommandProcessor() {
        if (mTab == null || mTab.getWebContents() == null) {
            return new CakeMediaCommandProcessor(null);
        }
        return new CakeMediaCommandProcessor(MediaSession.fromWebContents(mTab.getWebContents()));
    }
}
