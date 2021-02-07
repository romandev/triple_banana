// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaSecureDnsSettings;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.application.ApplicationLifeTime;
import org.triple.banana.settings.AdblockFeatureSettings;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;
import org.triple.banana.settings.MediaFeatureSettings;
import org.triple.banana.settings.UserInterfaceSettings;
import org.triple.banana.theme.DarkModeController;

import java.util.HashMap;
import java.util.Map;

class ButtonActionProviderImpl implements ButtonActionProvider {
    final private @NonNull Map<Integer, Runnable> mActionMap;

    ButtonActionProviderImpl() {
        mActionMap = new HashMap<Integer, Runnable>() {
            {
                put(R.id.adblock,
                        ()
                                -> BananaToolbarManager.get().openSettingPage(
                                        AdblockFeatureSettings.class));
                put(R.id.add_secret_tab, BananaToolbarManager.get()::addSecretTab);
                put(R.id.archive, BananaToolbarManager.get()::goArchive);
                put(R.id.close_tab, BananaToolbarManager.get()::closeCurrentTab);
                put(R.id.dark_mode, DarkModeController.get()::toggle);
                put(R.id.desktop_view, BananaToolbarManager.get()::changeDesktopMode);
                put(R.id.download, BananaToolbarManager.get()::download);
                put(R.id.find_in_page, BananaToolbarManager.get()::findInPage);
                put(R.id.new_tab, BananaToolbarManager.get()::addNewTab);
                put(R.id.print, BananaToolbarManager.get()::print);
                put(R.id.qr_code, BananaToolbarManager.get()::openQRCodeDialog);
                put(R.id.search, BananaToolbarManager.get()::search);
                put(R.id.secure_dns,
                        ()
                                -> BananaToolbarManager.get().openSettingPage(
                                        BananaSecureDnsSettings.class));
                put(R.id.share, BananaToolbarManager.get()::share);
                put(R.id.terminate, () -> (new ApplicationLifeTime()).terminate());
                put(R.id.translate, BananaToolbarManager.get()::translateCurrentTab);
                put(R.id.user_interface,
                        ()
                                -> BananaToolbarManager.get().openSettingPage(
                                        UserInterfaceSettings.class));
                put(R.id.visit_history, BananaToolbarManager.get()::goVisitHistory);
                put(R.id.banana_extension_settings,
                        () -> BananaToolbarManager.get().openSettingPage(ExtensionFeatures.class));
                put(R.id.media_feature,
                        ()
                                -> BananaToolbarManager.get().openSettingPage(
                                        MediaFeatureSettings.class));
            }
        };
    }

    @Override
    public @NonNull Runnable getAction(@IdRes int id) {
        if (mActionMap.containsKey(id)) {
            return mActionMap.get(id);
        }
        return () -> {};
    }
}
