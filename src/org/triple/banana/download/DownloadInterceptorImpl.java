// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;
import org.triple.banana.download.mojom.DownloadInterceptor;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public enum DownloadInterceptorImpl implements DownloadInterceptor {
    instance;

    @Override
    public void intercept(
            @NonNull String url, @NonNull String mimeType, @NonNull InterceptResponse callback) {
        if (!ExtensionFeatures.isEnabled(FeatureName.EXTERNAL_DOWNLOAD_MANAGER)) {
            callback.call(false /* wasIntercepted */);
            return;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, mimeType);
        Context context = BananaApplicationUtils.get().getApplicationContext();
        context.startActivity(
                Intent.createChooser(
                              intent, context.getResources().getString(R.string.menu_download))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        callback.call(true /* wasIntercepted */);
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<DownloadInterceptor> {
        public Factory() {}

        @Override
        public DownloadInterceptor createImpl() {
            return DownloadInterceptorImpl.instance;
        }
    }
}
