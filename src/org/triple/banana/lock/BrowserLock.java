
// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.lock;

import org.triple.banana.authentication.Authenticator;
import org.triple.banana.lock.ApplicationStatusTracker.ApplicationStatus;

public class BrowserLock {
    private static boolean isAuthenticated = false;
    public static void start() {
        ApplicationStatusTracker.getInstance().addListener((lastActivity, status) -> {
            if (status == ApplicationStatus.FOREGROUND) {
                Authenticator.get().authenticate(result -> {
                    // Currently, Callback is called twice when authentication done.(#546)
                    // 1. In case of success : true, false
                    // 2. In case of failure : false, false
                    // So, we should ignore second callback which is always false.
                    if (!result) {
                        if (!isAuthenticated) {
                            lastActivity.moveTaskToBack(true);
                        } else {
                            isAuthenticated = false;
                        }
                    } else {
                        isAuthenticated = true;
                    }
                });
            }
        });
    }
}