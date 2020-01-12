// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

import org.triple.banana.encrypter.mojom.EncrypterManager;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class EncrypterManagerImpl implements EncrypterManager {
    private Encrypter mEncrypter;

    private EncrypterManagerImpl() {
        mEncrypter = EncrypterFactory.create();
    }

    @Override
    public void encrypt(String plainText, EncryptResponse callback) {
        callback.call(mEncrypter.encrypt(plainText));
    }

    @Override
    public void decrypt(String cipherText, DecryptResponse callback) {
        callback.call(mEncrypter.decrypt(cipherText));
    }

    @Override
    public void getEncryptedDataFromPlainText(
            String plainText, GetEncryptedDataFromPlainTextResponse callback) {
        callback.call(mEncrypter.getEncryptedDataFromPlainText(plainText));
    }

    @Override
    public void getEncryptedDataFromCipherText(
            String cipherText, GetEncryptedDataFromCipherTextResponse callback) {
        callback.call(mEncrypter.getEncryptedDataFromCipherText(cipherText));
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<EncrypterManager> {
        public Factory() {}

        @Override
        public EncrypterManager createImpl() {
            return new EncrypterManagerImpl();
        }
    }
}
