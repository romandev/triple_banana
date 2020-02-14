// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.settings;

import android.content.Context;

import org.banana.cake.interfaces.BananaExtensionSettings;
import org.banana.cake.interfaces.BananaToolbarManager;

public class SettingsOpener implements BananaExtensionSettings {
    @Override
    public void open(Context context) {
        BananaToolbarManager.get().openSettingPage(ExtensionFeatures.class);
    }
}
