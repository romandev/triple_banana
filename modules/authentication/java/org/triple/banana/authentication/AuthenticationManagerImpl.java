// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.triple.banana.authentication.mojom.AuthenticationManager;

import org.chromium.base.ContextUtils;
import org.chromium.chrome.browser.payments.SslValidityChecker;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startAuthenticationActivity(BiometricPromptAuthenticationActivity.class);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startAuthenticationActivity(KeyguardAuthenticationActivity.class);
        } else {
            // If the user's device can't support any authenticator, just calls handleResult as
            // follows.
            handleResult(true);
        }
    }

    private void startAuthenticationActivity(Class activityClass) {
        Context context = ContextUtils.getApplicationContext();
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    static void handleResult(boolean result) {
        if (!isRunning()) return;
        sCallback.call(result);
        sCallback = null;
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
