// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import org.triple.banana.lock.BrowserLock;

public class KeyguardActivity extends BaseActivity {
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
            return;
        }

        KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguard == null) {
            finish();
            return;
        }

        Intent intent = keyguard.createConfirmDeviceCredentialIntent(null, null);
        if (intent == null) {
            finish();
        }

        BrowserLock.getInstance().pause();
        startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BrowserLock.getInstance().resume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            handleCallback(resultCode == RESULT_OK);
            finish();
        }
    }
}
