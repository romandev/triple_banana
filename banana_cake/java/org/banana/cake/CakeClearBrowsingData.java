// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaClearBrowsingData.BananaOnClearBrowsingDataListener;

import org.chromium.chrome.browser.browsing_data.BrowsingDataBridge;
import org.chromium.chrome.browser.browsing_data.BrowsingDataType;
import org.chromium.chrome.browser.browsing_data.TimePeriod;

public class CakeClearBrowsingData implements BananaClearBrowsingData {
    @Override
    public void clearBrowsingData(BananaOnClearBrowsingDataListener listener) {
        BrowsingDataBridge.getInstance().clearBrowsingData(listener,
                new int[] {
                        BrowsingDataType.HISTORY, BrowsingDataType.CACHE, BrowsingDataType.COOKIES},
                TimePeriod.ALL_TIME);
    }
}
