// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.chromium.chrome.browser.tab.Tab;

public interface BananaTab {
    static BananaTab get() {
        return BananaInterfaceProvider.get(BananaTab.class);
    }

    BananaTab wrap(Tab tab);

    String getUrl();
    void evaluateJavaScript(String script, BananaJavaScriptCallback callback);
    Context getContext();
    void exitFullscreen();
    @Nullable
    View getContentView();
    void setInfobarEnabled(boolean value);
}
