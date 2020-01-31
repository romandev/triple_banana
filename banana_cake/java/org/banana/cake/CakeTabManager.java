// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.banana.cake.interfaces.BananaTabObserver;

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
}
