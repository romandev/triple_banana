// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.triple.banana.authentication.Authenticator.Callback;

class FallbackBackend implements Backend {
    @Override
    public void authenticate(@NonNull FragmentActivity parent, @NonNull Callback callback) {
        callback.onResult(false);
    }

    @Override
    public void authenticate(
            @NonNull FragmentActivity parent, boolean isBackground, @NonNull Callback callback) {
        callback.onResult(false);
    }
}
