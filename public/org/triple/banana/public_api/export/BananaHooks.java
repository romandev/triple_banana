// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import org.triple.banana.hooks_api.ChromeHooks;
import org.triple.banana.hooks_api.ChromeHooksDelegate;
import org.triple.banana.public_api.export.BananaCommandLine;
import org.triple.banana.public_api.export.BananaTab;

import org.chromium.base.CommandLine;
import org.chromium.chrome.browser.tab.Tab;

public class BananaHooks {
    private Listener mListener = new Listener() {};
    private static BananaHooks sInstance;

    public static BananaHooks getInstance() {
        if (sInstance == null) {
            BananaHooksImpl impl = new BananaHooksImpl();
            sInstance = impl;
            ChromeHooks.setInstance(impl);
        }
        return sInstance;
    }

    public void setEventListener(Listener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    protected Listener getEventListener() {
        return mListener;
    }

    public interface Listener {
        default void onUrlUpdated(BananaTab tab) {}
        default void initCommandLine(BananaCommandLine line) {}
    }

    private static class BananaHooksImpl extends BananaHooks implements ChromeHooksDelegate {
        @Override
        public void onUrlUpdated(Tab tab) {
            getEventListener().onUrlUpdated(new BananaTab(tab));
        }

        @Override
        public void initCommandLine() {
            getEventListener().initCommandLine(BananaCommandLine.instance);
        }
    }
}
