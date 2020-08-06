// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.R;

class RemoteControlClient {
    static void onStart() {
        RemoteControlService.instance.addEventListener(new RemoteControlService.EventListener() {
            @Override
            public void onEnteredVideoFullscreen() {
                BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
                if (tab == null || tab.getContext() == null) return;
                MockRemoteControlView view = new MockRemoteControlView(
                        tab.getContext(), R.style.Theme_Chromium_Activity_Fullscreen_Transparent);
                view.show();
            }
        });
    }
}
