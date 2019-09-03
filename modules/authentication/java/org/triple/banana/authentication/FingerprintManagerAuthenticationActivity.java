// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintManagerAuthenticationActivity extends TranslucentActivity {
    // TODO(#56): Use FingerprintManagerCompat instead of FingerprintManager in authentication
    private FingerprintManager mFingerprintManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finish();
            return;
        }

        mFingerprintManager = this.getSystemService(FingerprintManager.class);

        if (isFingerprintAuthAvailable()) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            // TODO(#54): Inject cryptoObject for using private key
            mFingerprintManager.authenticate(
                    null, cancellationSignal, 0, new AuthenticationCallback(), null);
        }
    }

    private boolean isFingerprintAuthAvailable() {
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    private class AuthenticationCallback extends FingerprintManager.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errorString) {}

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {}

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {}

        @Override
        public void onAuthenticationFailed() {}
    }
}
