/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package org.triple.banana.media;

import android.text.TextUtils;
import android.util.Log;

import org.banana.cake.interfaces.BananaTab;

import java.net.MalformedURLException;
import java.net.URL;

public enum MediaSuspendController {
    instance;

    private static final String TAG = "MediaSuspendController";
    private static final String DISABLE_ON_YOUTUBE_SCRIPT = ""
            + "window.addEventListener('visibilitychange',"
            + "evt => evt.stopImmediatePropagation(), true);";

    public void DisableOnYouTube(BananaTab tab) {
        if (TextUtils.isEmpty(tab.getUrl())) {
            return;
        }

        try {
            URL url = new URL(tab.getUrl());
            if ("/watch".equalsIgnoreCase(url.getPath())) {
                final String host = url.getHost();
                if ("www.youtube.com".equalsIgnoreCase(host) || "youtube.com".equalsIgnoreCase(host)
                        || "m.youtube.com".equalsIgnoreCase(host)) {
                    tab.evaluateJavaScript(DISABLE_ON_YOUTUBE_SCRIPT);
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException " + e.getMessage());
        }
    }
}
