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
    private AlertDialog mErrorDialog;

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
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
    }

    private class AuthenticationCallback extends BiometricPrompt.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errorMessage) {
            switch (errorCode) {
                case ERROR_LOCKOUT_PERMANENT:
                case ERROR_LOCKOUT:
                    showErrorMessage(errorMessage);
                    return;
            }

            handleCallback(false);
            finish();
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            handleCallback(true);
            finish();
        }

        @Override
        public void onAuthenticationFailed() {}
    }

    private void showErrorMessage(CharSequence errorMessage) {
        if (mErrorDialog == null) {
            mErrorDialog =
                    new AlertDialog
                            .Builder(this,
                                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                            .setTitle(getResources().getString(R.string.authentication_title))
                            .setPositiveButton(getResources().getString(android.R.string.ok),
                                    (dialogInterface, i) -> {
                                        handleCallback(false);
                                        finish();
                                    })
                            .setCancelable(false)
                            .create();
        }

        if (!mErrorDialog.isShowing()) {
            mErrorDialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mErrorDialog.setMessage(errorMessage);
            mErrorDialog.show();
        }
    }
}
