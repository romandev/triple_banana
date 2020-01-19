// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.adblock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.triple.banana.adblock.mojom.FilterLoader;
import org.triple.banana.public_api.export.BananaContextUtils;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class FilterLoaderImpl implements FilterLoader {
    @SuppressLint("SdCardPath")
    @Override
    public void load(LoadResponse callback) {
        Context context = BananaContextUtils.getApplicationContext();
        if (context == null) return;

        AssetManager manager = context.getAssets();
        try (InputStream stream = manager.open("Filters")) {
            // TODO(zino): We should check version or meta information first.
            File filterPath = new File(getDataDir() + "/files/Filters");
            if (!copyFile(stream, filterPath)) return;
            callback.call(filterPath.getAbsolutePath());
            return;
        } catch (Exception e) {
        }
        callback.call(new String());
    }

    @SuppressLint("SdCardPath")
    private String getDataDir() {
        Context context = BananaContextUtils.getApplicationContext();
        if (context == null) return new String();
        File dataDir = ContextCompat.getDataDir(context);
        if (dataDir != null) return dataDir.getAbsolutePath();
        return "/data/data/" + context.getPackageName();
    }

    private boolean copyFile(InputStream inputStream, File file) {
        // Files.copy() would be much simpler than this, but is only available on API version 26+.
        try (FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<FilterLoader> {
        public Factory() {}

        @Override
        public FilterLoader createImpl() {
            return new FilterLoaderImpl();
        }
    }
}
