// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        return extract(targetZip, targetZip.getParentFile(), isOverwritten);
    }

    // TODO(bk_1.ko) : This api need to work on Worker thread instead of Main thread.
    public boolean extract(File targetZip, File destination, boolean isOverwritten) {
        // Do dry-run
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(targetZip))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                File outputPath = new File(destination.getPath(), entry.getName());
                if (!isOverwritten && outputPath.exists()) return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "extract(): Dry-run with " + e.toString());
            return false;
        }

        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(targetZip))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                File outputPath = new File(destination.getPath(), entry.getName());
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
        }
    }
}
