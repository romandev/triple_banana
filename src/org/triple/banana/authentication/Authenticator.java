// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;

public class Authenticator {
    private static Authenticator sInstance;
    private Backend mAuthenticator;
    private Backend mFallback;

    public static interface Callback { void onResult(boolean result); }

    public static Authenticator get() {
        if (sInstance == null) {
            sInstance = new Authenticator();
        }

        return sInstance;
    }

    public void authenticate(Callback callback) {
        mAuthenticator = createBackend();
        mAuthenticator.authenticate(callback);
    }

    public Authenticator setFallback(Backend fallback) {
        mFallback = fallback;
        return this;
    }

    private static Backend createBackend() {
        if (isBiometricsSecure()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return new ActivityBasedBackend(BiometricPromptActivity.class);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return new ActivityBasedBackend(FingerprintManagerActivity.class);
            }
        } else if (isKeyguardSecure()) {
            return new ActivityBasedBackend(KeyguardActivity.class);
        }

        return new FallbackBackend();
    }

    static boolean isBiometricsSecure() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return false;
        return FingerprintManagerCompat.from(context).isHardwareDetected()
                && FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    static boolean isKeyguardSecure() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return false;

        Object manager = context.getSystemService(Context.KEYGUARD_SERVICE);
        if (manager == null || !(manager instanceof KeyguardManager)) return false;

        return ((KeyguardManager) manager).isKeyguardSecure();
    }
}
