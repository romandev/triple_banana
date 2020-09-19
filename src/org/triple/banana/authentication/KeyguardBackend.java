// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.triple.banana.authentication.Authenticator.Callback;

public class KeyguardBackend implements Backend {
    @Override
    public void authenticate(@NonNull Callback callback) {
        authenticate(false, callback);
    }

    @Override
    public void authenticate(boolean isBackground, @NonNull Callback callback) {
        FragmentActivity parent =
                (FragmentActivity)
                        org.chromium.base.ApplicationStatus.getLastTrackedFocusedActivity();
        KeyguardFragment fragment = KeyguardFragment.create(isBackground, callback);
        fragment.show(parent.getSupportFragmentManager(), "background");
    }
}
