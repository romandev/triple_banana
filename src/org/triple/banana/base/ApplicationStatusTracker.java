// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.base;

import android.app.Activity;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityState;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityStateListener;

import java.util.HashSet;

/**
 * This class tracking application status(foreground/background) and notify events to listeners.
 */
public class ApplicationStatusTracker {
    private HashSet<ApplicationStatusListener> mListeners = new HashSet();

    public enum ApplicationStatus { FOREGROUND, BACKGROUND, UNSPECIFIED }
    ;

    // Variables needed for BananaApplcationStateListener
    private ApplicationStatus mLastStatus = ApplicationStatus.UNSPECIFIED;

    private BananaActivityStateListener mActivityStateListener = (activity, state) -> {
        if (state == BananaActivityState.RESUMED && mLastStatus != ApplicationStatus.FOREGROUND
                && BananaApplicationUtils.get().hasVisibleActivities()) {
            mLastStatus = ApplicationStatus.FOREGROUND;
            notifyListeners(activity, mLastStatus);
        } else if (state == BananaActivityState.STOPPED
                && mLastStatus == ApplicationStatus.FOREGROUND
                && !BananaApplicationUtils.get().hasVisibleActivities()) {
            mLastStatus = ApplicationStatus.BACKGROUND;
            notifyListeners(activity, mLastStatus);
        }
    };

    /**
     * Get ApplicationsStatusTracker singleton instance.
     * @return Instance of ApplicationStatusTracker
     */
    public static ApplicationStatusTracker getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static ApplicationStatusTracker INSTANCE = new ApplicationStatusTracker();
    }

    public void start() {
        BananaApplicationUtils.get().registerStateListenerForAllActivities(mActivityStateListener);
    }

    public void stop() {
        BananaApplicationUtils.get().unregisterActivityStateListener(mActivityStateListener);
    }

    /**
     * Add ApplicationStatusListener
     * @param listener
     */
    public void addListener(ApplicationStatusListener listener) {
        mListeners.add(listener);
    }

    /**
     * Remove ApplicationStatusListener
     * @param listener
     */
    public void removeListener(ApplicationStatusListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    private void notifyListeners(Activity activity, ApplicationStatus state) {
        for (ApplicationStatusListener listener : mListeners) {
            listener.onApplicationStatusChanged(activity, state);
        }
    }

    public interface ApplicationStatusListener {
        void onApplicationStatusChanged(Activity lastActivity, ApplicationStatus status);
    }
}
