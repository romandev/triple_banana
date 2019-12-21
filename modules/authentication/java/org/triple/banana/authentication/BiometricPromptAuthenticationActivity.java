// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;

@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.P)
public class BiometricPromptAuthenticationActivity extends TranslucentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            finish();
            return;
        }

        final BiometricPrompt prompt =
                new BiometricPrompt.Builder(this)
                        .setTitle("Authentication")
                        .setDescription("Scan your biometric data to sign-in your credentials")
                        .setNegativeButton(getResources().getText(android.R.string.cancel),
                                getMainExecutor(), (dialogInterface, i) -> { handleResult(false); })
                        .build();
        final CancellationSignal cancellationSignal = new CancellationSignal();
        prompt.authenticate(cancellationSignal, getMainExecutor(), new AuthenticationCallback());
    }

    private void handleResult(boolean result) {
        AuthenticationManagerImpl.handleResult(result);
        finish();
    }

    private class AuthenticationCallback extends BiometricPrompt.AuthenticationCallback {
        @Override
        @TargetApi(Build.VERSION_CODES.P)
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            // TODO(#140) Implement dialog for biometric prompt
            handleResult(false);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {}

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            handleResult(true);
        }

        @Override
        public void onAuthenticationFailed() {
        }
    }
}
