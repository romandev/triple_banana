// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import android.content.Context;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;

import org.triple.banana.hooks_api.ChromeHooks;
import org.triple.banana.hooks_api.ChromeHooksDelegate;
import org.triple.banana.public_api.export.BananaCommandLine;
import org.triple.banana.public_api.export.BananaTab;

import org.chromium.base.CommandLine;
import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.bottom.BottomToolbarCoordinator.BottomToolbarCoordinatorDelegate;

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
        default BottomToolbarCoordinatorDelegate createBottomToolbarCoordinatorDelegate(
                View root, ActivityTabProvider tabProvider) {
            return null;
        }
        default void startToolbarEditActivity(Context packageContext) {}
        default SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
            return null;
        }
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

        @Override
        public BottomToolbarCoordinatorDelegate createBottomToolbarCoordinatorDelegate(
                View root, ActivityTabProvider tabProvider) {
            return getEventListener().createBottomToolbarCoordinatorDelegate(root, tabProvider);
        }

        @Override
        public void startToolbarEditActivity(Context packageContext) {
            getEventListener().startToolbarEditActivity(packageContext);
        }

        @Override
        public SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
            return getEventListener().createAuthenticationSwitch(context);
        }
    }
}
