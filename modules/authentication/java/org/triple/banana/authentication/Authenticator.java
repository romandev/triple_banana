// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import org.banana.cake.interfaces.BananaContextUtils;

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

    private static boolean isBiometricsSecure() {
        Context context = BananaContextUtils.get().getApplicationContext();
        if (context == null) return false;
        return FingerprintManagerCompat.from(context).isHardwareDetected()
                && FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    private static boolean isKeyguardSecure() {
        Context context = BananaContextUtils.get().getApplicationContext();
        if (context == null) return false;

        Object manager = context.getSystemService(Context.KEYGUARD_SERVICE);
        if (manager == null || !(manager instanceof KeyguardManager)) return false;

        return ((KeyguardManager) manager).isKeyguardSecure();
    }
}
