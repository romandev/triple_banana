// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.content.Context;

import org.banana.cake.interfaces.BananaExtensionSettings;
import org.banana.cake.interfaces.BananaToolbarManager;

public class SettingsOpener implements BananaExtensionSettings {
    @Override
    public void open(Context context) {
        BananaToolbarManager.get().openSettingPage(NewExtensionFeatures.class);
    }
}
