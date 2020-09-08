// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityState;

import java.util.HashSet;
import java.util.Set;

public class SecurityLevelChecker {
    private static SecurityLevelChecker sInstance;
    private final Set<Listener> mListeners = new HashSet<>();
    private SecurityLevel mLastSecurityLevel = SecurityLevel.UNKNOWN;

    public enum SecurityLevel { UNKNOWN, SECURE, NON_SECURE }

    public static interface Listener { void onChange(SecurityLevel result); }

    public static SecurityLevelChecker get() {
        if (sInstance == null) {
            sInstance = new SecurityLevelChecker();
        }

        return sInstance;
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
        listener.onChange(mLastSecurityLevel);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public boolean isSecure() {
        checkAndUpdateSecurityLevel();
        return mLastSecurityLevel == SecurityLevel.SECURE;
    }

    private SecurityLevelChecker() {
        BananaApplicationUtils.get().registerStateListenerForAllActivities((activity, state) -> {
            if (state == BananaActivityState.RESUMED) {
                checkAndUpdateSecurityLevel();
            }
        });
        checkAndUpdateSecurityLevel();
    }

    private void checkAndUpdateSecurityLevel() {
        SecurityLevel newLevel =
                (Authenticator.isBiometricsSecure() || Authenticator.isKeyguardSecure())
                ? SecurityLevel.SECURE
                : SecurityLevel.NON_SECURE;
        if (mLastSecurityLevel == newLevel) return;

        mLastSecurityLevel = newLevel;
        for (Listener listener : mListeners) {
            listener.onChange(newLevel);
        }
    }
}
