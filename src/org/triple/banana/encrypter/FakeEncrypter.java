// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

import org.triple.banana.encrypter.mojom.EncryptedData;
import org.triple.banana.encrypter.mojom.EncryptedDataHeader;

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

    @Override
    public EncryptedData getEncryptedDataFromPlainText(String plainText) {
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.header = new EncryptedDataHeader();
        encryptedData.cipherData = plainText.getBytes();
        encryptedData.hashedText = plainText;
        return encryptedData;
    }

    @Override
    public EncryptedData getEncryptedDataFromCipherText(String cipherText) {
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.header = new EncryptedDataHeader();
        encryptedData.cipherData = cipherText.getBytes();
        encryptedData.hashedText = cipherText;
        return encryptedData;
    }
}
