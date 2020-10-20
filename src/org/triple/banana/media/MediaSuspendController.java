/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package org.triple.banana.media;

import org.banana.cake.interfaces.BananaTab;
import org.triple.banana.util.YouTubeUtil;

public enum MediaSuspendController {
    instance;

    private static final String TAG = "MediaSuspendController";
    private static final String DISABLE_ON_YOUTUBE_SCRIPT = ""
            + "window.addEventListener('visibilitychange',"
            + "evt => evt.stopImmediatePropagation(), true);";

    public void DisableOnYouTube(BananaTab tab) {
        if (YouTubeUtil.isYouTubeDomainUrl(tab.getUrl())) {
            tab.evaluateJavaScript(DISABLE_ON_YOUTUBE_SCRIPT, null);
        }
    }
}
