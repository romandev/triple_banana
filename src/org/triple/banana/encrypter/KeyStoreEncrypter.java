// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.encrypter;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import org.triple.banana.encrypter.mojom.EncryptedData;
import org.triple.banana.encrypter.mojom.EncryptedDataHeader;
import org.triple.banana.encrypter.mojom.EncrypterVersion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@TargetApi(Build.VERSION_CODES.M)
public class KeyStoreEncrypter implements Encrypter {
    private static final int ENCRYPTER_VERSION = EncrypterVersion.VERSION_01;
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
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(null);

            if (!keyStore.containsAlias(keyStoreAlias)) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA, KEY_STORE_TYPE);
                KeyGenParameterSpec.Builder builder =
                        new KeyGenParameterSpec
                                .Builder(keyStoreAlias,
                                        KeyProperties.PURPOSE_ENCRYPT
                                                | KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(
                                        KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .setUserAuthenticationRequired(userAuthenticationRequired);
                keyPairGenerator.initialize(builder.build());
                keyPairGenerator.generateKeyPair();
            }
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

    private String getEncrypterAlgo(int encrypterVersion) {
        if (encrypterVersion == EncrypterVersion.VERSION_01) {
            return KeyProperties.KEY_ALGORITHM_RSA + "/" + KeyProperties.BLOCK_MODE_ECB + "/"
                    + KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1;
        } else {
            // NOTREACHED
            Log.e(TAG, "Not supported encrypter version");
            return new String();
        }
    }

    private String getHashString(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
            digest.update(password.getBytes(StandardCharsets.UTF_8.name()));
            // byte to hex
            return String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // NOTREACHED
            Log.e(TAG, "Failed to get hash from password");
        }
        return new String();
    }

    @Override
    public String encrypt(String plainText) {
        // FIXME(#160): Need a good way on how to convert from byte buffer to string
        EncryptedData encryptedData = getEncryptedDataFromPlainText(plainText);
        ByteBuffer writer = ByteBuffer.allocate(encryptedData.serialize().capacity());
        writer.put(encryptedData.serialize());
        return Base64.encodeToString(writer.array(), Base64.DEFAULT);
    }

    @Override
    public String decrypt(String cipherText) {
        try {
            EncryptedData encryptedData = getEncryptedDataFromCipherText(cipherText);
            Cipher decryptCipher =
                    Cipher.getInstance(getEncrypterAlgo(encryptedData.header.version));
            KeyStore.PrivateKeyEntry pkEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyStoreAlias, null);
            decryptCipher.init(Cipher.DECRYPT_MODE, pkEntry.getPrivateKey());
            return new String(decryptCipher.doFinal(encryptedData.cipherData));
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            Log.e(TAG, "decrypt() failed");
        }
        return null;
    }

    @Override
    public EncryptedData getEncryptedDataFromPlainText(String plainText) {
        try {
            Cipher encryptCipher = Cipher.getInstance(getEncrypterAlgo(ENCRYPTER_VERSION));
            KeyStore.PrivateKeyEntry pkEntry =
                    (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyStoreAlias, null);
            encryptCipher.init(Cipher.ENCRYPT_MODE, pkEntry.getCertificate().getPublicKey());
            EncryptedDataHeader encryptedDataHeader = new EncryptedDataHeader();
            encryptedDataHeader.version = ENCRYPTER_VERSION;
            EncryptedData encryptedData = new EncryptedData();
            encryptedData.header = encryptedDataHeader;
            encryptedData.cipherData = encryptCipher.doFinal(plainText.getBytes());
            encryptedData.hashedText = getHashString(plainText);
            return encryptedData;
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            Log.e(TAG, "getEncryptedDataFromPlainText() failed");
        }
        return null;
    }

    @Override
    public EncryptedData getEncryptedDataFromCipherText(String cipherText) {
        return EncryptedData.deserialize(
                ByteBuffer.wrap(Base64.decode(cipherText, Base64.DEFAULT)));
    }
}
