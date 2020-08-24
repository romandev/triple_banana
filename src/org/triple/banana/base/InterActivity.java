// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.util.HashMap;
import java.util.Map;

public class InterActivity<Request, Response> extends FragmentActivity {
    public interface Callback<Response> { void onResponse(Response response); }

    private static final Map<Integer, Object> REQUEST_DATA = new HashMap<>();
    private static final Map<Integer, Object> CALLBACKS = new HashMap<>();
    private static int sRequestId;

    private Request mRequestData;
    private Callback<Response> mCallback;

    public static <R> void start(Class<? extends InterActivity> activityClass, Object requestData,
            Callback<R> callback) {
        sRequestId++;

        REQUEST_DATA.put(sRequestId, requestData);
        CALLBACKS.put(sRequestId, callback);

        Context context = BananaApplicationUtils.get().getApplicationContext();
        Intent intent = new Intent(context, activityClass);
        intent.putExtra("__request_id", sRequestId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    final protected Request getRequestData() {
        return mRequestData;
    }

    final protected void handleCallback(Response response) {
        mCallback.onResponse(response);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (getIntent() == null && getIntent().getExtras() == null) return;

        int requestId = getIntent().getExtras().getInt("__request_id");
        mRequestData = (Request) REQUEST_DATA.get(sRequestId);
        mCallback = (Callback<Response>) CALLBACKS.get(sRequestId);
        REQUEST_DATA.remove(sRequestId);
        CALLBACKS.remove(sRequestId);
    }
}
