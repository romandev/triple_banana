// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import org.triple.banana.authentication.Authenticator.Callback;

class FallbackBackend implements Backend {
    @Override
    public void authenticate(Callback callback) {
        callback.onResult(false);
    }
}
