// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDownloader {
    private static SimpleDownloader sInstance;
    public static SimpleDownloader get() {
        if (sInstance == null) {
            sInstance = new SimpleDownloader();
        }

        return sInstance;
    }

    public class Option {
        public boolean isBackground = true;
        public boolean isOverwritten = true;
    }

    public interface Callback { void onResult(File path); }

    private SimpleDownloader() {}

    public void download(final Uri targetUri, final Callback callback) {
        download(targetUri, new Option(), callback);
    }

    private BroadcastReceiver mReceiver;
    private void registerReceiverIfNeeded() {
        // NOTE: There is no API to check whether the receiver is already registered or not. So,
        // we assume that the mReceiver would be already registered anyway if it has already been
        // created.
        if (mReceiver != null) return;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (!mRequestMap.containsKey(id)) return;

                mRequestMap.get(id).onResult(null);
                mRequestMap.remove(id);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BananaApplicationUtils.get().getApplicationContext().registerReceiver(mReceiver, filter);
    }

    private final ConcurrentHashMap<Long, Callback> mRequestMap = new ConcurrentHashMap<>();
    public void download(final Uri targetUri, final Option option, final Callback callback) {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) {
            callback.onResult(null);
            return;
        }
        registerReceiverIfNeeded();

        File destinationFile =
                new File(context.getExternalFilesDir(null), targetUri.getLastPathSegment());
        if (destinationFile.exists() && option.isOverwritten) {
            boolean success = destinationFile.delete();
            if (!success) {
                callback.onResult(null);
                return;
            }
        }

        DownloadManager manager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager == null) {
            callback.onResult(null);
            return;
        }

        DownloadManager.Request request =
                new DownloadManager.Request(targetUri)
                        .setNotificationVisibility(option.isBackground
                                        ? DownloadManager.Request.VISIBILITY_HIDDEN
                                        : DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(destinationFile));
        long id = manager.enqueue(request);
        mRequestMap.put(id, callback);
    }
}
