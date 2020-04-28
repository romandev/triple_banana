// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.secure_dns;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;

public enum SecureDnsNotificationManager {
    instance;

    private static final String TAG = "SecureDnsNotificationManager";
    private static final String CHANNEL_SECURE_DNS_IMPORTANCE_HIGH = "secure_dns_high";
    private static final String CHANNEL_SECURE_DNS_IMPORTANCE_LOW = "secure_dns_low";
    private static final int SECURE_DNS_TABS_OPEN_ID = 10000;
    private final NotificationManager mNotificationManager;
    private boolean mIsFirstTime;
    private boolean mIsShowing;

    SecureDnsNotificationManager() {
        final Context context = BananaApplicationUtils.get().getApplicationContext();
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        mIsFirstTime = true;
        mIsShowing = false;
    }

    public static SecureDnsNotificationManager getInstance() {
        return instance;
    }

    public void showSecureDnsNotification() {
        final Context context = BananaApplicationUtils.get().getApplicationContext();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        String title = context.getResources().getString(R.string.secure_dns_notification_title);
        String message = context.getResources().getString(R.string.secure_dns_notification_message);
        NotificationCompat.Builder builder;
        if (mIsFirstTime) {
            // For heads up notification when first run.
            builder = new NotificationCompat.Builder(context, CHANNEL_SECURE_DNS_IMPORTANCE_HIGH)
                              .setPriority(NotificationCompat.PRIORITY_HIGH)
                              .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
            mIsFirstTime = false;
        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_SECURE_DNS_IMPORTANCE_LOW)
                              .setPriority(NotificationCompat.PRIORITY_LOW);
        }
        builder.setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.toolbar_button_banana);
        mNotificationManager.notify(TAG, SECURE_DNS_TABS_OPEN_ID, builder.build());
        mIsShowing = true;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public void dismissSecureDnsNotification() {
        mNotificationManager.cancel(TAG, SECURE_DNS_TABS_OPEN_ID);
        mIsShowing = false;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Label for notification that indicates secure dns mode is active.
        String name = "Secure DNS";
        NotificationChannel highChannel = new NotificationChannel(
                CHANNEL_SECURE_DNS_IMPORTANCE_HIGH, name, NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel lowChannel = new NotificationChannel(
                CHANNEL_SECURE_DNS_IMPORTANCE_LOW, name, NotificationManager.IMPORTANCE_LOW);
        mNotificationManager.createNotificationChannel(highChannel);
        mNotificationManager.createNotificationChannel(lowChannel);
    }
}
