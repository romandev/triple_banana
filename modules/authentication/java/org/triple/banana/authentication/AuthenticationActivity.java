// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

public class AuthenticationActivity extends Activity {
    private boolean mResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button ok = new Button(this);
        ok.setText(android.R.string.ok);
        ok.setOnClickListener((v) -> {
            mResult = true;
            finish();
        });

        Button cancel = new Button(this);
        cancel.setText(android.R.string.cancel);
        cancel.setOnClickListener((v) -> {
            mResult = false;
            finish();
        });

        layout.addView(ok);
        layout.addView(cancel);
        setContentView(layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AuthenticationManagerImpl.handleResult(mResult);
    }
}
