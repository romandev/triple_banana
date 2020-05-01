// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana;

import android.content.Context;

import org.banana.cake.bootstrap.BananaApplication;
import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaTabManager;
import org.triple.banana.media.MediaSuspendController;
import org.triple.banana.password.PasswordExtension;
import org.triple.banana.secure_dns.SecureDnsNotificationManager;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class TripleBananaApplication extends BananaApplication {
    static {
        InterfaceProvider.initialize();
    }

    private void initializeOnBrowser() {
        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            BananaTabManager.get().addObserver(
                    bananaTab -> { MediaSuspendController.instance.DisableOnYouTube(bananaTab); });
        }

        if (ExtensionFeatures.isEnabled(FeatureName.SECURE_DNS)) {
            SecureDnsNotificationManager.getInstance().showSecureDnsNotification();
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (isBrowserProcess()) {
            initializeOnBrowser();
        }
    }
}
