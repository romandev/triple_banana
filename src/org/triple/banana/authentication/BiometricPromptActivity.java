// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
                        .setTitle(getResources().getString(R.string.authentication_title))
                        .setDescription(
                                getResources().getString(R.string.authentication_description))
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
                            .setTitle(getResources().getString(R.string.authentication_title))
                            .setPositiveButton(getResources().getString(android.R.string.ok),
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
