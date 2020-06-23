// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {
    private static final String TAG = "Unzip";
    private static Unzip sInstance;

    public static Unzip get() {
        if (sInstance == null) sInstance = new Unzip();
        return sInstance;
    }

    public boolean extract(File targetZip, boolean isOverwritten) {
        try (InputStream stream = new FileInputStream(targetZip)) {
            return extract(stream, targetZip.getParentFile(), isOverwritten);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean extract(File targetZip, File destination, boolean isOverwritten) {
        try (InputStream stream = new FileInputStream(targetZip)) {
            return extract(stream, destination.getParentFile(), isOverwritten);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMarkSupported(InputStream targetStream) {
        try {
            return targetStream.markSupported();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean extract(InputStream targetStream, File destination, boolean isOverwritten) {
        try (InputStream markSupportedStream = targetStream.markSupported()
                        ? targetStream
                        : new BufferedInputStream(targetStream)) {
            markSupportedStream.mark(10000000);
            return extractInternal(markSupportedStream, destination, isOverwritten);
        } catch (Exception e) {
            return false;
        }
    }

    // TODO(bk_1.ko) : This api need to work on Worker thread instead of Main thread.
    private boolean extractInternal(
            InputStream targetStream, File destination, boolean isOverwritten) {
        ZipInputStream dryRunZipStream = null;
        ZipInputStream zipStream = null;
        try {
            dryRunZipStream = new ZipInputStream(targetStream);
            ZipEntry entry;

            // Do dry-run
            while ((entry = dryRunZipStream.getNextEntry()) != null) {
                File outputPath = new File(destination.getPath(), entry.getName());

                // We need to check whether the output path is canonical path in order to avoid zip
                // traversal attack. Please see the following link in more details.
                //   - https://support.google.com/faqs/answer/929400
                if (!outputPath.getCanonicalPath().startsWith(destination.getPath())) return false;
                if (!isOverwritten && outputPath.exists()) return false;
            }

            targetStream.reset();
            zipStream = new ZipInputStream(targetStream);

            while ((entry = zipStream.getNextEntry()) != null) {
                File outputPath = new File(destination.getPath(), entry.getName());

                // We already checked that the output path is canonical above. So, it should always
                // be `true` here.
                assert outputPath.getCanonicalPath().startsWith(destination.getPath());

                if (entry.isDirectory()) {
                    if (!outputPath.exists() && !outputPath.mkdirs()) return false;
                    continue;
                }

                try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "extract(): " + e.toString());
            return false;
        } finally {
            try {
                if (dryRunZipStream != null) dryRunZipStream.close();
                if (zipStream != null) zipStream.close();
            } catch (Exception e) {
            }
        }
    }
}
