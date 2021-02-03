// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import static androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.FragmentActivity;

import org.banana.cake.interfaces.BananaApplicationUtils;

public class Authenticator {
    private static Authenticator sInstance;
    private Backend mAuthenticator;
    private Backend mFallback;

    @FunctionalInterface
    public static interface Callback {
        void onResult(boolean result);
    }

    public static Authenticator get() {
        if (sInstance == null) {
            sInstance = new Authenticator();
        }

        return sInstance;
    }

    public void authenticate(@NonNull Callback callback) {
        Activity parent = BananaApplicationUtils.get().getLastTrackedFocusedActivity();
        if (!(parent instanceof FragmentActivity)) {
            callback.onResult(false);
            return;
        }
        authenticate((FragmentActivity) parent, callback);
    }

    public void authenticate(@NonNull FragmentActivity parent, @NonNull Callback callback) {
        mAuthenticator = createBackend();
        mAuthenticator.authenticate(parent, callback);
    }

    void authenticateWithKeyguardAsFallback(@NonNull FragmentActivity parent,
            boolean hasOpaqueBackground, @NonNull Callback callback) {
        mAuthenticator = new KeyguardBackend();
        mAuthenticator.authenticate(parent, hasOpaqueBackground, callback);
    }

    public void authenticate(
            @NonNull FragmentActivity parent, boolean isBackground, @NonNull Callback callback) {
        mAuthenticator = createBackend();
        mAuthenticator.authenticate(parent, true, callback);
    }

    public Authenticator setFallback(Backend fallback) {
        mFallback = fallback;
        return this;
    }

    private static Backend createBackend() {
        if (isBiometricsSecure()) {
            return new BiometricPromptBackend();
        } else if (isKeyguardSecure()) {
            return new KeyguardBackend();
        }

        return new FallbackBackend();
    }

    static boolean isBiometricsSecure() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return false;
        return BiometricManager.from(context).canAuthenticate() == BIOMETRIC_SUCCESS;
    }

    static boolean isKeyguardSecure() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return false;

        Object manager = context.getSystemService(Context.KEYGUARD_SERVICE);
        if (manager == null || !(manager instanceof KeyguardManager)) return false;

        return ((KeyguardManager) manager).isKeyguardSecure();
    }
}
