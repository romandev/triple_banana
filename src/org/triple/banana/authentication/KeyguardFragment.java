// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.triple.banana.authentication.Authenticator.Callback;

import java.util.HashMap;
import java.util.Map;

public class KeyguardFragment extends DialogFragment {
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private static @NonNull Map<Integer, Callback> sCallbacks = new HashMap<>();
    private static @NonNull Integer sCallbackId = 0;
    private @NonNull Handler mHandler = new Handler();

    public static KeyguardFragment create(boolean hasOpaqueBackground, @NonNull Callback callback) {
        KeyguardFragment fragment = new KeyguardFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean("has_opaque_background", hasOpaqueBackground);
        arguments.putInt("callback_id", sCallbackId);
        fragment.setArguments(arguments);
        sCallbacks.put(sCallbackId, callback);
        sCallbackId++;
        return fragment;
    }

    private void onKeyguardStart() {
        boolean hasOpaqueBackground = getArguments().getBoolean("has_opaque_background", true);
        if (hasOpaqueBackground) {
            setCancelable(false);
            setStyle(
                    DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        }

        KeyguardManager keyguard =
                (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguard == null) {
            onKeyguardResult(false);
            return;
        }

        Intent intent = keyguard.createConfirmDeviceCredentialIntent(null, null);
        if (intent == null) {
            onKeyguardResult(false);
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
    }

    private void onKeyguardResult(boolean result) {
        boolean hasOpaqueBackground = getArguments().getBoolean("has_opaque_background", true);
        if (result || !hasOpaqueBackground) {
            dismiss();
        }

        int callbackId = getArguments().getInt("callback_id", -1);
        Callback callback = sCallbacks.get(callbackId);
        if (callback != null) {
            sCallbacks.remove(callbackId);
            callback.onResult(result);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onKeyguardStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            onKeyguardResult(resultCode == Activity.RESULT_OK);
        }
    }
}
