// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.encrypter;

import org.triple.banana.encrypter.mojom.EncryptedData;

/**
 * Abstract encrypter interface. All concrete encrypters should implement this interface's APIs.
 */
interface Encrypter {
    /**
     * @param plainText plain text to encrypt
     * @return cipher text which contains encrypted plain text and how to encrypt the data
     */
    String encrypt(String plainText);

    /**
     * @param cipherText cipher text which contains encrypted plain text and how to encrypt the data
     * @return plain text to decrypt
     */
    String decrypt(String cipherText);

    /**
     * @param plainText plain text to encrypt
     * @return encryptedData structure which contains encrypterVersion, cipherData and hashedText
     *         for comparison with old password
     */
    EncryptedData getEncryptedDataFromPlainText(String plainText);

    /**
     * @param cipherText cipher text which contains encrypted plain text and how to encrypt the data
     * @return encryptedData structure which contains encrypterVersion, cipherData and hashedText
     *         for comparison with old password
     */
    EncryptedData getEncryptedDataFromCipherText(String cipherText);
}
