// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT_PERMANENT;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.LinearLayout;

import org.triple.banana.R;

@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.P)
public class BiometricPromptActivity extends BaseActivity {
    private AlertDialog mLockoutDialog;
    private CancellationSignal mCancellationSignal;

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
        mCancellationSignal = new CancellationSignal();
        prompt.authenticate(mCancellationSignal, getMainExecutor(), new AuthenticationCallback());
    }

    @Override
    public void onPause() {
        super.onPause();
        handleResult(false);
    }

    private void handleResult(boolean result) {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
        if (mLockoutDialog != null && mLockoutDialog.isShowing()) {
            mLockoutDialog.dismiss();
        }
        handleCallback(result);
        finish();
    }

    private class AuthenticationCallback extends BiometricPrompt.AuthenticationCallback {
        @Override
        @TargetApi(Build.VERSION_CODES.P)
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            if (errorCode == BIOMETRIC_ERROR_LOCKOUT_PERMANENT) {
                showLockout(getResources().getString(R.string.authentication_lockout_permenent));
            } else if (errorCode == BIOMETRIC_ERROR_LOCKOUT) {
                showLockout(getResources().getString(R.string.authentication_lockout));
            } else {
                handleResult(false);
            }
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {}

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            handleResult(true);
        }

        @Override
        public void onAuthenticationFailed() {}
    }

    private void showLockout(CharSequence lockoutString) {
        if (mLockoutDialog == null) {
            mLockoutDialog =
                    new AlertDialog
                            .Builder(this,
                                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                            .setTitle(getResources().getString(R.string.authentication_error))
                            .setPositiveButton(
                                    getResources().getString(R.string.authentication_check),
                                    (dialogInterface, i) -> { handleResult(false); })
                            .setCancelable(false)
                            .create();
        }

        if (!mLockoutDialog.isShowing()) {
            mLockoutDialog.setMessage(lockoutString);
            mLockoutDialog.show();
            mLockoutDialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}
