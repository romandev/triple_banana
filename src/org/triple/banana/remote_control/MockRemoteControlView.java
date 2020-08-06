// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import org.triple.banana.R;

class MockRemoteControlView extends Dialog implements View.OnClickListener {
    MockRemoteControlView(@NonNull Context context) {
        super(context);
    }

    MockRemoteControlView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.mock_remote_control_view);
        addClickListener();
    }

    private void addClickListener() {
        findViewById(R.id.play_button).setOnClickListener(this);
        findViewById(R.id.pause_button).setOnClickListener(this);
        findViewById(R.id.backward_button).setOnClickListener(this);
        findViewById(R.id.forward_button).setOnClickListener(this);
        findViewById(R.id.brightness_up_button).setOnClickListener(this);
        findViewById(R.id.brightness_down_button).setOnClickListener(this);
        findViewById(R.id.volume_up_button).setOnClickListener(this);
        findViewById(R.id.volume_down_button).setOnClickListener(this);
        findViewById(R.id.rotate_button).setOnClickListener(this);
        findViewById(R.id.lock_button).setOnClickListener(this);
        findViewById(R.id.seek_bar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.play_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.pause_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.backward_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.forward_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.brightness_down_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.brightness_up_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.volume_down_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.volume_up_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.rotate_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.lock_button) {
            // NOTIMPLEMENTED
        } else if (id == R.id.seek_bar) {
            // NOTIMPLEMENTED
        }
    }
}
