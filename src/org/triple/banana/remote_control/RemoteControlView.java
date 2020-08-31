// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.triple.banana.remote_control.RemoteControlLayout;

interface RemoteControlView {
    enum Effect { NONE, FORWARD, BACKWARD }

    static interface Delegate
            extends RemoteControlGestureDetector.Callback, RemoteControlLayout.Listener {
        void onCancel();
        void onRemoteControlButtonClicked(int id);
    }

    void show(@NonNull Activity parentActivity);
    void dismiss();
    void showEffect(Effect effect);
}
