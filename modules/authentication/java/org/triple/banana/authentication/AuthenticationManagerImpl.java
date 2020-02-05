// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import org.triple.banana.authentication.mojom.AuthenticationManager;

import org.chromium.base.ContextUtils;
import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class AuthenticationManagerImpl implements AuthenticationManager {
    private static AuthenticateResponse sCallback;

    @Override
    public void authenticate(AuthenticateResponse callback) {
        if (isRunning()) return;

        sCallback = callback;
        startAuthenticationActivity();
    }

    private static boolean isRunning() {
        return sCallback != null;
    }

    private void startAuthenticationActivity() {
        if (!isAuthenticationEnabled()) {
            // TODO(#169): Remove all saved data when biometric and keyguard data is
            // unregistered
            handleResult(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (isFingerPrintSupportedDevice() && isBiometricDataRegistered()) {
                startAuthenticationActivity(BiometricPromptAuthenticationActivity.class);
            } else if (isKeyguardSecured()) {
                startAuthenticationActivity(KeyguardAuthenticationActivity.class);
            } else {
                // Fallback
                handleResult(false);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isFingerPrintSupportedDevice() && isBiometricDataRegistered()
                    && isKeyguardSecured()) {
                startAuthenticationActivity(FingerprintManagerAuthenticationActivity.class);
            } else if (isFingerPrintSupportedDevice() && isBiometricDataRegistered()) {
                startAuthenticationActivity(FingerprintManagerAuthenticationActivity.class);
            } else if (isKeyguardSecured()) {
                startAuthenticationActivity(KeyguardAuthenticationActivity.class);
            } else {
                // Fallback
                handleResult(false);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKeyguardSecured()) {
                startAuthenticationActivity(KeyguardAuthenticationActivity.class);
            } else {
                // Fallback
                handleResult(false);
            }
        } else {
            // If the user's device can't support any authenticator, just calls handleResult as
            // follows.
            handleResult(false);
        }
    }

    private void startAuthenticationActivity(Class activityClass) {
        Context context = ContextUtils.getApplicationContext();
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean isAuthenticationEnabled() {
        SharedPreferences pref = ContextUtils.getApplicationContext().getSharedPreferences(
                "org.triple.banana_preferences", Context.MODE_PRIVATE);
        return pref.getBoolean("authentication_switch", false);
    }

    static void handleResult(boolean result) {
        if (!isRunning()) return;
        sCallback.call(result);
        sCallback = null;
    }

    private boolean isFingerPrintSupportedDevice() {
        Context context = ContextUtils.getApplicationContext();
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    private boolean isBiometricDataRegistered() {
        Context context = ContextUtils.getApplicationContext();
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    private boolean isKeyguardSecured() {
        Context context = ContextUtils.getApplicationContext();
        return ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE))
                .isKeyguardSecure();
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<AuthenticationManager> {
        public Factory() {}

        @Override
        public AuthenticationManager createImpl() {
            return new AuthenticationManagerImpl();
        }
    }
}
