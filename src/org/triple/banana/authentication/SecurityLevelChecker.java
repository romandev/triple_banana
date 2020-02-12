// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityState;

import java.util.HashSet;
import java.util.Set;

public class SecurityLevelChecker {
    private static SecurityLevelChecker sInstance;
    private final Set<Listener> mListeners = new HashSet<>();
    private SecurityLevel mLastSecurityLevel;

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
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    private SecurityLevelChecker() {
        BananaApplicationUtils.get().registerStateListenerForAllActivities((activity, state) -> {
            if (state == BananaActivityState.RESUMED) {
                checkAndUpdateSecurityLevel();
            }
        });
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
