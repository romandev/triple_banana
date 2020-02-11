// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

import android.os.Build;

class EncrypterFactory {
    static Encrypter create() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return KeyStoreEncrypter.create("TripleBanana");
        } else {
            return FakeEncrypter.create();
        }
    }
}
