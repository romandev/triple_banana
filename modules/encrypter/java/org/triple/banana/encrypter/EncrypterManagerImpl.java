// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.encrypter;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import org.triple.banana.encrypter.mojom.EncrypterManager;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

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
public class EncrypterManagerImpl implements EncrypterManager {
    private boolean mUserAuthenticationRequired;
    private KeyStore mKeyStore;
    private PrivateKey mPrivateKey;
    private PublicKey mPublicKey;
    private String mKeyName;

    private EncrypterManagerImpl() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        } catch (NoSuchAlgorithmException | CertificateException | IOException
                | KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder =
                    new KeyGenParameterSpec
                            .Builder(mKeyName,
                                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            // Set true if we want to use fingerprint
                            .setUserAuthenticationRequired(mUserAuthenticationRequired);

            keyPairGenerator.initialize(builder.build());
            keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(String keyName, boolean userAuthenticationRequired) {
        mKeyName = keyName;
        mUserAuthenticationRequired = userAuthenticationRequired;
    }

    @Override
    public void encrypt(String plainText, EncryptResponse callback) {
        try {
            if (!mKeyStore.containsAlias(mKeyName)) {
                generateKeyPair();
            }
            if (mPublicKey == null) {
                KeyStore.PrivateKeyEntry pkEntry =
                        (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyName, null);
                mPublicKey = pkEntry.getCertificate().getPublicKey();
            }
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());
            callback.call(Base64.encodeToString(cipherText, Base64.DEFAULT));
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void decrypt(String cipherText, DecryptResponse callback) {
        try {
            if (!mKeyStore.containsAlias(mKeyName)) {
                generateKeyPair();
            }
            if (mPrivateKey == null) {
                KeyStore.PrivateKeyEntry pkEntry =
                        (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mKeyName, null);
                mPrivateKey = pkEntry.getPrivateKey();
            }
            final byte[] bytes = Base64.decode(cipherText, Base64.DEFAULT);
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, mPrivateKey);
            callback.call(new String(decryptCipher.doFinal(bytes)));
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
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
