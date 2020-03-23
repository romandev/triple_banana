// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import static android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT;
import static android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.triple.banana.R;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintManagerActivity extends BaseActivity {
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private final Handler mHandler = new Handler();

    private FingerprintManagerCompat mFingerprintManager;
    private Button mCancelButton;
    private TextView mAuthenticationDescriptionText;
    private CancellationSignal mCancellationSignal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finish();
            return;
        }

        // Setting for finguerprint ui
        setContentView(R.layout.fingerprint_dialog);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(layoutParams);
        setFinishOnTouchOutside(false);

        mAuthenticationDescriptionText = findViewById(R.id.authentication_description_text);
        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener((View v) -> { handleResult(false); });
        mFingerprintManager = FingerprintManagerCompat.from(this);

        if (isFingerprintAuthAvailable()) {
            mCancellationSignal = new CancellationSignal();
            // TODO(#54): Inject cryptoObject for using private key
            mFingerprintManager.authenticate(
                    null, 0, mCancellationSignal, new AuthenticationCallback(), null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handleResult(false);
    }

    private boolean isFingerprintAuthAvailable() {
        return FingerprintManagerCompat.from(this).isHardwareDetected()
                && FingerprintManagerCompat.from(this).hasEnrolledFingerprints();
    }

    private void handleResult(boolean result) {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
        handleCallback(result);
        finish();
    }

    private class AuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errorString) {
            if (errorCode == FINGERPRINT_ERROR_LOCKOUT_PERMANENT) {
                showLockout(getResources().getString(R.string.authentication_lockout_permenent));
            } else if (errorCode == FINGERPRINT_ERROR_LOCKOUT) {
                showLockout(getResources().getString(R.string.authentication_lockout));
            } else {
                showError(errorString);
            }
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            showError(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(
                FingerprintManagerCompat.AuthenticationResult result) {
            handleResult(true);
        }

        @Override
        public void onAuthenticationFailed() {
            showError(getResources().getString(R.string.authentication_not_recognized));
        }

        private void showError(CharSequence errorString) {
            mHandler.removeCallbacksAndMessages(null);
            mAuthenticationDescriptionText.setText(errorString);
            mAuthenticationDescriptionText.setTextColor(
                    getResources().getColor(R.color.authentication_warning_color));
            mHandler.postDelayed(() -> {
                mAuthenticationDescriptionText.setText(
                        getResources().getString(R.string.authentication_description));
                mAuthenticationDescriptionText.setTextColor(
                        getResources().getColor(R.color.authentication_hint_color));
            }, ERROR_TIMEOUT_MILLIS);
        }

        private void showLockout(CharSequence lockoutString) {
            mHandler.removeCallbacksAndMessages(null);
            mAuthenticationDescriptionText.setText(lockoutString);
            mAuthenticationDescriptionText.setTextColor(
                    getResources().getColor(R.color.authentication_warning_color));
            mAuthenticationDescriptionText.setGravity(Gravity.CENTER);
            mAuthenticationDescriptionText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mCancelButton.setText(android.R.string.ok);
        }
    }
}
