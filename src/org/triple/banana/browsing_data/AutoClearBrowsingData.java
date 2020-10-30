// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.browsing_data;

import org.banana.cake.interfaces.BananaApplicationEvent;
import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class AutoClearBrowsingData implements BananaApplicationEvent {
    @Override
    public void onTerminate() {
        if (ExtensionFeatures.isEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA)) {
            BananaClearBrowsingData.get().clearBrowsingData(null);
        }
    }
}
