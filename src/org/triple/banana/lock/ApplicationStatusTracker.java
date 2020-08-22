// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.lock;

import android.app.Activity;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityState;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityStateListener;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaApplicationState;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaApplicationStateListener;

import java.util.HashSet;

/**
 * This class tracking application status(foreground/background) and notify events to listeners.
 */
public class ApplicationStatusTracker {
    private HashSet<ApplicationStatusListener> mListeners = new HashSet();

    public enum ApplicationStatus { FOREGROUND, BACKGROUND, UNSPECIFIED }
    ;

    // Variables needed for BananaApplcationStateListener
    private boolean mNeedToNotify = false;
    private ApplicationStatus mCurrentStatus = ApplicationStatus.UNSPECIFIED;
    private Activity mLastTrackedActivity = null;

    private BananaApplicationStateListener mApplicationStateListener = (state) -> {
        ApplicationStatus newStatus = isApplicationInForeground(state)
                ? ApplicationStatus.FOREGROUND
                : ApplicationStatus.BACKGROUND;
        if (newStatus != mCurrentStatus) {
            mCurrentStatus = newStatus;
            if (mCurrentStatus == ApplicationStatus.FOREGROUND) {
                // In case of turning to foreground from background, if try to start activty, it
                // doesn't work. So we just store value indicates notification is required, and
                // when activity resumed, check this value and do something.
                mNeedToNotify = true;
            } else {
                notifyListeners(mLastTrackedActivity, mCurrentStatus);
            }
        }
    };
    private BananaActivityStateListener mActivityStateListener = (activity, state) -> {
        if (state == BananaActivityState.RESUMED && mNeedToNotify) {
            mNeedToNotify = false;
            assert mCurrentStatus == ApplicationStatus.FOREGROUND;
            notifyListeners(activity, mCurrentStatus);
        } else if (state == BananaActivityState.PAUSED) {
            mLastTrackedActivity = activity;
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
        BananaApplicationUtils.get().registerApplicationStateListener(mApplicationStateListener);
        BananaApplicationUtils.get().registerStateListenerForAllActivities(mActivityStateListener);
    }

    public void stop() {
        BananaApplicationUtils.get().unregisterApplicationStateListener(mApplicationStateListener);
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

    private static boolean isApplicationInForeground(int state) {
        return state == BananaApplicationState.HAS_RUNNING_ACTIVITIES
                || state == BananaApplicationState.HAS_PAUSED_ACTIVITIES;
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