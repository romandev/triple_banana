// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import static androidx.biometric.BiometricPrompt.ERROR_CANCELED;
import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT;
import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT_PERMANENT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;
import org.triple.banana.authentication.Authenticator.Callback;

import java.lang.ref.WeakReference;

public class BiometricPromptBackend
        extends BiometricPrompt.AuthenticationCallback implements Backend {
    private @Nullable Dialog mBackground;
    private @Nullable Callback mCallback;
    private @Nullable WeakReference<FragmentActivity> mParent;

    @Override
    public void authenticate(@NonNull FragmentActivity parent, @NonNull Callback callback) {
        authenticate(parent, false, callback);
    }

    @Override
    public void authenticate(
            @NonNull FragmentActivity parent, boolean isBackground, @NonNull Callback callback) {
        mParent = new WeakReference<>(parent);
        mCallback = callback;
        if (isBackground) {
            mBackground = new Dialog(parent, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            mBackground.show();
        }
        authenticateInternal(parent);
    }

    private void authenticateInternal(@NonNull FragmentActivity parent) {
        // FIXME(#711): Consider a new way to change the biometric prompt builder description
        boolean isBackground = mBackground != null;
        PromptInfo promptInfo =
                new PromptInfo.Builder()
                        .setTitle(parent.getResources().getString(R.string.authentication_title))
                        .setDescription(parent.getResources().getString(isBackground
                                        ? R.string.browser_lock_description
                                        : R.string.authentication_description))
                        .setNegativeButtonText(
                                parent.getResources().getText(android.R.string.cancel))
                        .build();
        BiometricPrompt prompt =
                new BiometricPrompt(parent, ContextCompat.getMainExecutor(parent), this);
        prompt.authenticate(promptInfo);
    }

    private void handleCallback(boolean result) {
        if (mCallback == null) return;

        if (result && mBackground != null) {
            mBackground.dismiss();
        }

        mCallback.onResult(result);
        mCallback = null;
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errorMessage) {
        switch (errorCode) {
            case ERROR_CANCELED:
                if (mParent == null || mParent.get() == null) return;
                if (Authenticator.isKeyguardSecure()) {
                    assert mCallback != null;
                    Callback callback = mCallback;
                    mCallback = null;
                    boolean hasOpaqueBackground = mBackground != null;
                    Authenticator.get().authenticateWithKeyguardAsFallback(
                            mParent.get(), hasOpaqueBackground, callback);
                    return;
                }
                break;
            case ERROR_LOCKOUT_PERMANENT:
            case ERROR_LOCKOUT:
                showErrorMessage(errorMessage);
                return;
        }
        handleCallback(false);
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        handleCallback(true);
    }

    @Override
    public void onAuthenticationFailed() {}

    private void showErrorMessage(CharSequence errorMessage) {
        Activity parent = BananaApplicationUtils.get().getLastTrackedFocusedActivity();
        if (parent == null) return;
        AlertDialog errorDialog =
                new AlertDialog
                        .Builder(parent,
                                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                        .setTitle(parent.getResources().getString(R.string.authentication_title))
                        .setPositiveButton(parent.getResources().getString(android.R.string.ok),
                                (dialogInterface, i) -> { handleCallback(false); })
                        .setCancelable(false)
                        .create();

        errorDialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        errorDialog.setMessage(errorMessage);
        errorDialog.show();
    }
}
