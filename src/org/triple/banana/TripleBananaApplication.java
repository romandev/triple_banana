// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import android.content.Context;

import org.banana.cake.bootstrap.BananaApplication;
import org.banana.cake.interfaces.BananaTabManager;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.media.MediaSuspendController;
import org.triple.banana.password.PasswordExtension;

public class TripleBananaApplication extends BananaApplication {
    static {
        InterfaceProvider.initialize();
    }

    private void initializeOnBrowser() {
        BananaTabManager.get().addObserver(
                bananaTab -> { MediaSuspendController.instance.DisableOnYouTube(bananaTab); });
        SecurityLevelChecker.get().addListener(PasswordExtension::onSecurityLevelChanged);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (isBrowserProcess()) {
            initializeOnBrowser();
        }
    }
}
