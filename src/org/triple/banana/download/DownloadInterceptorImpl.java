// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.download;

import androidx.annotation.NonNull;

import org.triple.banana.download.mojom.DownloadInterceptor;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public enum DownloadInterceptorImpl implements DownloadInterceptor {
    instance;

    @Override
    public void intercept(
            @NonNull String url, @NonNull String mimeType, @NonNull InterceptResponse callback) {
        callback.call(false /* wasIntercepted */);
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
