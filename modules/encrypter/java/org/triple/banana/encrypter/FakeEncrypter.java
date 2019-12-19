// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

class FakeEncrypter implements Encrypter {
    static Encrypter create() {
        return new FakeEncrypter();
    }

    private FakeEncrypter() {}

    @Override
    public String encrypt(String plainText) {
        return plainText;
    }

    @Override
    public String decrypt(String cipherText) {
        return cipherText;
    }
}
