// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.app.Activity;

import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.banana.cake.interfaces.BananaTabObserver;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeTabbedActivity;
import org.chromium.chrome.browser.tab.Tab;

import java.util.HashSet;
import java.util.Set;

class CakeTabManager implements BananaTabManager {
    private static final Set<BananaTabObserver> TAB_OBSERVERS = new HashSet<>();

    @Override
    public void addObserver(BananaTabObserver observer) {
        TAB_OBSERVERS.add(observer);
    }

    @Override
    public void removeObserver(BananaTabObserver observer) {
        TAB_OBSERVERS.remove(observer);
    }

    @Override
    public void notifyUrlUpdated(Tab tab) {
        for (BananaTabObserver observer : TAB_OBSERVERS) {
            observer.onUrlUpdated(BananaTab.get().wrap(tab));
        }
    }

    @Override
    public BananaTab getActivityTab() {
        Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
        if (!(activity instanceof ChromeTabbedActivity)) return null;
        return BananaTab.get().wrap(((ChromeTabbedActivity) activity).getActivityTab());
    }
}
