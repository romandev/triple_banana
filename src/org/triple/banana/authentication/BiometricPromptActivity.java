// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT;
import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT_PERMANENT;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.content.ContextCompat;

import org.triple.banana.R;

public class BiometricPromptActivity extends BaseActivity {
    private AlertDialog mLockoutDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PromptInfo promptInfo =
                new PromptInfo.Builder()
                        .setTitle(getResources().getString(R.string.authentication_title))
                        .setDescription(
                                getResources().getString(R.string.authentication_description))
                        .setNegativeButtonText(getResources().getText(android.R.string.cancel))
                        .build();
        final BiometricPrompt prompt = new BiometricPrompt(
                this, ContextCompat.getMainExecutor(this), new AuthenticationCallback());
        prompt.authenticate(promptInfo);
    }

    @Override
    public void onPause() {
        super.onPause();
        handleResult(false);
    }

    private void handleResult(boolean result) {
        if (mLockoutDialog != null && mLockoutDialog.isShowing()) {
            mLockoutDialog.dismiss();
        }
        handleCallback(result);
        finish();
    }

    private class AuthenticationCallback extends BiometricPrompt.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            if (errorCode == ERROR_LOCKOUT_PERMANENT) {
                showLockout(getResources().getString(R.string.authentication_lockout_permenent));
            } else if (errorCode == ERROR_LOCKOUT) {
                showLockout(getResources().getString(R.string.authentication_lockout));
            } else {
                handleResult(false);
            }
        }

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
