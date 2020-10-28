// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana;

import android.content.Context;
import android.content.SharedPreferences;

import org.banana.cake.bootstrap.BananaApplication;
import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaTabManager;
import org.triple.banana.adblock.FilterLoader;
import org.triple.banana.appmenu.AppMenuDelegate;
import org.triple.banana.base.ApplicationStatusTracker;
import org.triple.banana.lock.BrowserLock;
import org.triple.banana.media.MediaSuspendController;
import org.triple.banana.media_remote.MediaRemoteService;
import org.triple.banana.password.PasswordExtension;
import org.triple.banana.secure_dns.SecureDnsNotificationManager;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class TripleBananaApplication extends BananaApplication {
    static {
        InterfaceProvider.initialize();
    }

    private boolean isFirstInstall() {
        return !BananaApplicationUtils.get().getSharedPreferences().contains(
                FeatureName.BOTTOM_TOOLBAR);
    }

    private String getCurrentVersion() {
        try {
            Context context = BananaApplicationUtils.get().getApplicationContext();
            if (context == null) return "";

            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        return "";
    }

    private String getLastUpdatedVersion() {
        return BananaApplicationUtils.get().getSharedPreferences().getString(
                "last_updated_version_name", "");
    }

    private void setLastUpdatedVersion(String version) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putString("last_updated_version_name", version);
        editor.apply();
    }

    @Override
    protected void onInitialized() {
        if (isFirstInstall()) {
            setLastUpdatedVersion(getCurrentVersion());
        }
        if (!getLastUpdatedVersion().equals(getCurrentVersion())) {
            AppMenuDelegate.get().setNewFeatureIcon(true);
            setLastUpdatedVersion(getCurrentVersion());
            FilterLoader.instance.forceUpdate();
        } else {
            FilterLoader.instance.updateIfNeeded();
        }

        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            BananaTabManager.get().addObserver(
                    bananaTab -> { MediaSuspendController.instance.DisableOnYouTube(bananaTab); });
        }

        if (ExtensionFeatures.isEnabled(FeatureName.SECURE_DNS)) {
            SecureDnsNotificationManager.getInstance().showSecureDnsNotificationIfNeeded();
        }

        MediaRemoteService.instance.start();
        ApplicationStatusTracker.getInstance().start();

        // Apply BrowserLock from ExtensionFeatures setting
        if (ExtensionFeatures.isEnabled(FeatureName.BROWSER_LOCK)) {
            BrowserLock.getInstance().start();
        }
    }
}
