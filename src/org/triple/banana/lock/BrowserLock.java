// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.lock;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.authentication.Authenticator;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.base.ApplicationStatusTracker;
import org.triple.banana.base.ApplicationStatusTracker.ApplicationStatus;
import org.triple.banana.base.ApplicationStatusTracker.ApplicationStatusListener;

public class BrowserLock {
    private boolean isExceptional = false;

    // BrowserLock Session
    private Long mLastAuthenticationTime;
    private static final long SESSION_DURATION = 60000;

    /**
     * Get BrowserLock singleton instance.
     * @return Instance of BrowserLock
     */
    public static BrowserLock getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static final class LazyHolder { private static BrowserLock INSTANCE = new BrowserLock(); }

    private ApplicationStatusListener mListener = (lastActivity, status) -> {
        if (status == ApplicationStatus.FOREGROUND) {
            if (!SecurityLevelChecker.get().isSecure()) {
                stop();
                return;
            }
            if (!isSessionExpired()) {
                return;
            }
            resetLastAuthenticationTime();

            if (isExceptional) {
                isExceptional = false;
                return;
            }
            Authenticator.get().authenticateWithBackground(result -> {
                if (result) {
                    recoredLastAuthenticationTime();
                } else {
                    // NOTE: The ApplicationStatusTracker's event is triggered asynchronously.
                    // Therefore, if the app restarts quickly, the background/foreground events
                    // might not be detected. In this case, the BrowserLock might be broken because
                    // the foreground event is not detected when re-entering the app again. So, we
                    // should reset the state explicitly in this case.
                    ApplicationStatusTracker.getInstance().reset();
                    BananaApplicationUtils.get().shutdown();
                }
            });
        }
    };

    public void start() {
        resetLastAuthenticationTime();
        resume();
    }

    public void stop() {
        resetLastAuthenticationTime();
        pause();
    }

    public void resume() {
        ApplicationStatusTracker.getInstance().addListener(mListener);
    }

    public void pause() {
        ApplicationStatusTracker.getInstance().removeListener(mListener);
    }

    public void setExceptional(boolean isException) {
        isExceptional = isException;
    }

    private void recoredLastAuthenticationTime() {
        mLastAuthenticationTime = System.currentTimeMillis();
    }

    private void resetLastAuthenticationTime() {
        mLastAuthenticationTime = null;
    }

    private boolean isSessionExpired() {
        if (mLastAuthenticationTime == null) {
            return true;
        }
        Long currentTimestamp = System.currentTimeMillis();
        return (currentTimestamp - mLastAuthenticationTime > SESSION_DURATION)
                || (currentTimestamp < mLastAuthenticationTime);
    }
}
