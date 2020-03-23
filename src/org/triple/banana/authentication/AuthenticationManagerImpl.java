// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import org.triple.banana.authentication.mojom.AuthenticationManager;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class AuthenticationManagerImpl implements AuthenticationManager {
    private static AuthenticateResponse sCallback;

    @Override
    public void authenticate(AuthenticateResponse callback) {
        if (isRunning()) return;

        sCallback = callback;

        if (!ExtensionFeatures.isEnabled(FeatureName.SECURE_LOGIN)) {
            handleResult(true);
            return;
        }

        Authenticator.get().authenticate(AuthenticationManagerImpl::handleResult);
    }

    private static boolean isRunning() {
        return sCallback != null;
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
