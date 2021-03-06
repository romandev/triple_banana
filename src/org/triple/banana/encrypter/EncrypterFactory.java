// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
