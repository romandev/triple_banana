// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;

import org.banana.cake.bootstrap.BananaApplication;
import org.triple.banana.public_api.export.BananaHooks;
import org.triple.banana.public_api.export.BananaTab;
import org.triple.banana.toolbar.BottomToolbarController;
import org.triple.banana.toolbar.ToolbarEditActivity;

import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.bottom.BottomToolbarCoordinator.BottomToolbarCoordinatorDelegate;

public class TripleBananaApplication extends BananaApplication {
    static {
        InterfaceProvider.initialize();
    }

    private static boolean sWasInitialized;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!sWasInitialized) {
            sWasInitialized = onInitializeHooks(BananaHooks.getInstance());
        }
    }

    public boolean onInitializeHooks(BananaHooks hooks) {
        hooks.setEventListener(new BananaHooks.Listener() {
            @Override
            public void onUrlUpdated(BananaTab tab) {
                MediaSuspendController.instance.DisableOnYouTube(tab);
            }

            @Override
            public BottomToolbarCoordinatorDelegate createBottomToolbarCoordinatorDelegate(
                    View root, ActivityTabProvider tabProvider) {
                // TODO(bk_1.ko) : we should implement toolbarmanager getter
                BottomToolbarController.createBottomToolbarController(root, tabProvider);

                return BottomToolbarController.getInstance();
            }

            @Override
            public void startToolbarEditActivity(Context packageContext) {
                Intent intent = new Intent(packageContext, ToolbarEditActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

            @Override
            public SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
                return SharedPreferenceController.instance.createAuthenticationSwitch(context);
            }
        });

        return true;
    }
}
