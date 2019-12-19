// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@TargetApi(Build.VERSION_CODES.M)
public class KeyStoreEncrypter implements Encrypter {
    private static final String TAG = "KeyStoreEncrypter";
    private static final String KEY_STORE_TYPE = "AndroidKeyStore";

    private final KeyStore mKeyStore;
    private final String mKeyStoreAlias;

    public static KeyStoreEncrypter create(String keyStoreAlias) {
        return create(keyStoreAlias, false);
    }

    public static KeyStoreEncrypter create(
            String keyStoreAlias, boolean userAuthenticationRequired) {
        try {
            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEY_STORE_TYPE);
            KeyGenParameterSpec.Builder builder =
                    new KeyGenParameterSpec
                            .Builder(keyStoreAlias,
                                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .setUserAuthenticationRequired(userAuthenticationRequired);
            keyPairGenerator.initialize(builder.build());
            keyPairGenerator.generateKeyPair();

            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(null);
            return new KeyStoreEncrypter(keyStore, keyStoreAlias);
        } catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException
                | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Failed to get an instance of KeyStore");
        }
        return null;
    }

    private KeyStoreEncrypter(KeyStore keyStore, String keyStoreAlias) {
        mKeyStore = keyStore;
        mKeyStoreAlias = keyStoreAlias;
    }

    @Override
    public String encrypt(String plainText) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyStore.PrivateKeyEntry pkEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyStoreAlias, null);
            encryptCipher.init(Cipher.ENCRYPT_MODE, pkEntry.getCertificate().getPublicKey());
            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            Log.e(TAG, "encrypt() failed");
        }
        return null;
    }

    @Override
    public String decrypt(String cipherText) {
        try {
            final byte[] bytes = Base64.decode(cipherText, Base64.DEFAULT);
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyStore.PrivateKeyEntry pkEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyStoreAlias, null);
            decryptCipher.init(Cipher.DECRYPT_MODE, pkEntry.getPrivateKey());
            return new String(decryptCipher.doFinal(bytes));
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            Log.e(TAG, "decrypt() failed");
        }
        return null;
    }
}
