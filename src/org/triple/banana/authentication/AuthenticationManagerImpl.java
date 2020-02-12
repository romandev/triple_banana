// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.authentication.mojom.AuthenticationManager;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class AuthenticationManagerImpl implements AuthenticationManager {
    private static AuthenticateResponse sCallback;

    @Override
    public void authenticate(AuthenticateResponse callback) {
        if (isRunning()) return;

        sCallback = callback;

        if (!isAuthenticatorEnabled()) {
            handleResult(true);
            return;
        }

        Authenticator.get().authenticate(AuthenticationManagerImpl::handleResult);
    }

    private static boolean isRunning() {
        return sCallback != null;
    }

    private static boolean isAuthenticatorEnabled() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                "is_authentication_enabled", false);
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
