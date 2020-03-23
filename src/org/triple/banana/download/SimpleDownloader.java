// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDownloader {
    private static final String TAG = "SimpleDownloader";
    private static SimpleDownloader sInstance;
    private final ConcurrentHashMap<Long, Callback> mCallbackMap = new ConcurrentHashMap<>();
    private final PersistentRequestIdMap mRequestIdMap = new PersistentRequestIdMap();
    private BroadcastReceiver mReceiver;

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

    private void registerReceiverIfNeeded() {
        // NOTE: There is no API to check whether the receiver is already registered or not. So,
        // we assume that the mReceiver would be already registered anyway if it has already been
        // created.
        if (mReceiver != null) return;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) return;

                // Do garbage collection for stored IDs
                for (Map.Entry<String, ?> entry : mRequestIdMap.getAll().entrySet()) {
                    if (!(entry.getValue() instanceof Long)) continue;

                    long storedId = (long) entry.getValue();
                    if (isFinished(storedId)) {
                        Log.i(TAG, "BroadcastReceiver: Do garbage collection " + storedId);
                        mRequestIdMap.remove(entry.getKey());
                    }
                }

                long requestId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (!isFinished(requestId) || !mCallbackMap.containsKey(requestId)) {
                    Log.e(TAG, "BroadcastReceiver: Cancel " + requestId);
                    return;
                }

                mCallbackMap.get(requestId).onResult(getDownloadFile(requestId));
                mCallbackMap.remove(requestId);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BananaApplicationUtils.get().getApplicationContext().registerReceiver(mReceiver, filter);
    }

    private boolean isFinished(long id) {
        long status = queryDownloadStatus(id);
        return status == DownloadManager.STATUS_SUCCESSFUL
                || status == DownloadManager.STATUS_FAILED;
    }

    private DownloadManager getDownloadManager() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        if (context == null) return null;

        DownloadManager manager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager;
    }

    private File getDownloadFile(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        DownloadManager manager = getDownloadManager();
        if (manager == null) return null;

        try (Cursor cursor = manager.query(query)) {
            if (cursor == null || !cursor.moveToFirst() || cursor.getCount() < 1) {
                return null;
            }

            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if (status != DownloadManager.STATUS_SUCCESSFUL) return null;

            columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            String localUri = cursor.getString(columnIndex);
            if (localUri == null) return null;

            return new File(Uri.parse(localUri).getPath());
        }
    }

    private int queryDownloadStatus(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        DownloadManager manager = getDownloadManager();
        if (manager == null) return DownloadManager.ERROR_UNKNOWN;

        try (Cursor cursor = manager.query(query)) {
            if (cursor == null || !cursor.moveToFirst() || cursor.getCount() < 1) {
                return DownloadManager.ERROR_UNKNOWN;
            }

            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            return cursor.getInt(columnIndex);
        }
    }

    private static class PersistentRequestIdMap {
        static final long INVALID_ID = -1;
        private SharedPreferences mPersistentStore;

        private PersistentRequestIdMap() {
            Context context = BananaApplicationUtils.get().getApplicationContext();
            if (context == null) return;
            mPersistentStore = context.getSharedPreferences(
                    "org.triple.banana.persistent_request_id_map", Context.MODE_PRIVATE);
        }

        void put(final Uri targetUri, long requestId) {
            if (mPersistentStore == null) return;
            SharedPreferences.Editor editor = mPersistentStore.edit();
            editor.putLong(targetUri.toString(), requestId);
            editor.apply();
        }

        long get(final Uri targetUri) {
            if (mPersistentStore == null) return INVALID_ID;
            return mPersistentStore.getLong(targetUri.toString(), INVALID_ID);
        }

        Map<String, ?> getAll() {
            if (mPersistentStore == null) return new HashMap<String, Long>();
            return mPersistentStore.getAll();
        }

        void remove(String targetUriString) {
            if (mPersistentStore == null) return;
            SharedPreferences.Editor editor = mPersistentStore.edit();
            editor.remove(targetUriString);
            editor.commit();
        }
    }

    public void download(final Uri targetUri, final Option option, final Callback callback) {
        DownloadManager manager = getDownloadManager();
        if (manager == null) {
            Log.e(TAG, "download(): getDownloadManager() is null");
            callback.onResult(null);
            return;
        }

        registerReceiverIfNeeded();

        long existingId = mRequestIdMap.get(targetUri);
        if (existingId != PersistentRequestIdMap.INVALID_ID && !isFinished(existingId)) {
            if (option.isOverwritten) {
                manager.remove(existingId);
            } else {
                // NOTE: If isOverwritten is false, then we can wait for the download result from
                // existing pending download request.
                mCallbackMap.put(existingId, callback);
                return;
            }
        }

        Context context = BananaApplicationUtils.get().getApplicationContext();
        File destinationFile =
                new File(context.getExternalFilesDir(null), targetUri.getLastPathSegment());
        if (destinationFile.exists() && option.isOverwritten && !destinationFile.delete()) {
            Log.e(TAG, "download(): Overwriting file is failed");
            callback.onResult(null);
            return;
        }

        int visibility = option.isBackground ? DownloadManager.Request.VISIBILITY_HIDDEN
                                             : DownloadManager.Request.VISIBILITY_VISIBLE;
        DownloadManager.Request request = new DownloadManager.Request(targetUri)
                                                  .setNotificationVisibility(visibility)
                                                  .setDestinationUri(Uri.fromFile(destinationFile));
        long requestId = manager.enqueue(request);
        mCallbackMap.put(requestId, callback);
        mRequestIdMap.put(targetUri, requestId);
    }
}
