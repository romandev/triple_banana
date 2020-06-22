// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preferences;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.preference.Preference;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.remote_config.RemoteConfig;

public class BananaNoticePreference extends Preference {
    private static final String LAST_CHECK_TIME_KEY = "banana_notice_last_check_time";
    private static final long UPDATE_INTERVAL = AlarmManager.INTERVAL_DAY;
    private RemoteConfig mRemoteConfig =
            new RemoteConfig("https://zino.dev/triple_banana_config/remote_config.json");

    private long getLastCheckTime() {
        return BananaApplicationUtils.get().getSharedPreferences().getLong(LAST_CHECK_TIME_KEY, 0);
    }

    private void updateLastCheckTime() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putLong(LAST_CHECK_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    private boolean shouldShowBananaNotice() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(getKey(), false);
    }

    private void setBananaNoticeVisible(boolean isVisible) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(getKey(), isVisible);
        editor.apply();
    }

    private void showNoticeIfNeeded() {
        if (!shouldShowBananaNotice()) {
            setVisible(false);
            return;
        }
        SpannableString spannableTitle = new SpannableString("BLACK LIVES MATTER");
        spannableTitle.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableTitle.length(), 0);
        spannableTitle.setSpan(
                new BackgroundColorSpan(Color.YELLOW), 0, spannableTitle.length(), 0);
        spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableTitle.length(), 0);
        spannableTitle.setSpan(new ForegroundColorSpan(Color.YELLOW), 6, 11, 0);
        spannableTitle.setSpan(new BackgroundColorSpan(Color.BLACK), 6, 11, 0);
        setTitle(spannableTitle);
        setOnPreferenceClickListener(preference -> {
            BananaApplicationUtils.get().showInfoPage("https://bit.ly/black_lives_matter_banner");
            return true;
        });
    }

    public BananaNoticePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        long now = System.currentTimeMillis();
        if (now > getLastCheckTime() + UPDATE_INTERVAL) {
            mRemoteConfig.getAsync(config -> {
                boolean isRemoteEnabled = config.optBoolean("banana_notice");
                setBananaNoticeVisible(isRemoteEnabled);
                showNoticeIfNeeded();
                updateLastCheckTime();
            });
        } else {
            showNoticeIfNeeded();
        }
    }
}
