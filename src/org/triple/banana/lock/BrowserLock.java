// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.lock;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.triple.banana.authentication.Authenticator;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.base.ApplicationStatusTracker;
import org.triple.banana.base.ApplicationStatusTracker.ApplicationStatus;
import org.triple.banana.base.ApplicationStatusTracker.ApplicationStatusListener;

public class BrowserLock {
    // BrowserLock Session
    private Long mLastAuthenticationTime;
    private static final long SESSION_DURATION = 60000;
    private @NonNull Handler mHandler = new Handler();

    /**
     * Get BrowserLock singleton instance.
     * @return Instance of BrowserLock
     */
    public static BrowserLock getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static final class LazyHolder { private static BrowserLock INSTANCE = new BrowserLock(); }

    private ApplicationStatusListener mListener = (lastActivity, status) -> {
        if (!(lastActivity instanceof FragmentActivity)) return;

        if (status == ApplicationStatus.FOREGROUND) {
            if (!SecurityLevelChecker.get().isSecure()) {
                stop();
                return;
            }
            if (!isSessionExpired()) {
                return;
            }
            resetLastAuthenticationTime();

            Authenticator.get().authenticate((FragmentActivity) lastActivity, true, result -> {
                if (result) {
                    recoredLastAuthenticationTime();
                } else {
                    // NOTE: The ApplicationStatusTracker's event is triggered asynchronously.
                    // Therefore, if the app restarts quickly, the background/foreground events
                    // might not be detected. In this case, the BrowserLock might be broken because
                    // the foreground event is not detected when re-entering the app again. So, we
                    // should reset the state explicitly in this case
                    ApplicationStatusTracker.getInstance().reset();
                    lastActivity.finishAffinity();
                }
            });
        }
    };

    public void start() {
        resetLastAuthenticationTime();
        ApplicationStatusTracker.getInstance().addListener(mListener);
    }

    public void stop() {
        resetLastAuthenticationTime();
        ApplicationStatusTracker.getInstance().removeListener(mListener);
    }

    public void pauseForAMoment() {
        ApplicationStatusTracker.getInstance().removeListener(mListener);
        mHandler.postDelayed(
                () -> { ApplicationStatusTracker.getInstance().addListener(mListener); }, 1000);
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
