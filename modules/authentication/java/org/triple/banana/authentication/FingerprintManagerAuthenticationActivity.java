// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintManagerAuthenticationActivity extends TranslucentActivity {
    private FingerprintManagerCompat mFingerprintManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finish();
            return;
        }

        mFingerprintManager = FingerprintManagerCompat.from(this);

        if (isFingerprintAuthAvailable()) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            // TODO(#54): Inject cryptoObject for using private key
            mFingerprintManager.authenticate(
                    null, 0, cancellationSignal, new AuthenticationCallback(), null);
        }
    }

    private boolean isFingerprintAuthAvailable() {
        return FingerprintManagerCompat.from(this).isHardwareDetected()
                && FingerprintManagerCompat.from(this).hasEnrolledFingerprints();
    }

    private void handleResult(boolean result) {
        AuthenticationManagerImpl.handleResult(result);
        finish();
    }

    private class AuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errorString) {
            handleResult(false);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {}

        @Override
        public void onAuthenticationSucceeded(
                FingerprintManagerCompat.AuthenticationResult result) {
            handleResult(true);
        }

        @Override
        public void onAuthenticationFailed() {
            handleResult(false);
        }
    }
}
