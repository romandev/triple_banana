// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;
import org.triple.banana.theme.DarkModeController;

import java.util.HashMap;
import java.util.Map;

class QuickMenuActionProviderImpl implements QuickMenuActionProvider {
    final private @NonNull Map<Integer, Action> mActionMap;

    QuickMenuActionProviderImpl() {
        mActionMap = new HashMap<Integer, Action>() {
            {
                put(R.id.add_secret_tab, BananaToolbarManager.get()::addSecretTab);
                put(R.id.add_to_home, BananaToolbarManager.get()::addToHomeScreen);
                put(R.id.archive, BananaToolbarManager.get()::goArchive);
                put(R.id.at_me_game,
                        ()
                                -> BananaApplicationUtils.get().showInfoPage(
                                        "https://www.atmegame.com/?utm_source=Banana&utm_medium=Banana"));
                put(R.id.back, BananaToolbarManager.get()::back);
                put(R.id.clear_data, BananaToolbarManager.get()::openClearBrowsingDataPreference);
                put(R.id.close_tab, BananaToolbarManager.get()::closeCurrentTab);
                put(R.id.dark_mode, DarkModeController.get()::toggle);
                put(R.id.desktop_view, BananaToolbarManager.get()::changeDesktopMode);
                put(R.id.download, BananaToolbarManager.get()::download);
                put(R.id.find_in_page, BananaToolbarManager.get()::findInPage);
                put(R.id.forward, BananaToolbarManager.get()::forward);
                put(R.id.new_tab, BananaToolbarManager.get()::addNewTab);
                put(R.id.print, BananaToolbarManager.get()::print);
                put(R.id.qr_code, BananaToolbarManager.get()::openQRCodeDialog);
                put(R.id.reload, BananaToolbarManager.get()::reload);
                put(R.id.search, BananaToolbarManager.get()::search);
                put(R.id.share, BananaToolbarManager.get()::share);
                put(R.id.terminate, () -> {
                    if (ExtensionFeatures.isEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA)) {
                        BananaClearBrowsingData.get().clearBrowsingData(
                                () -> BananaToolbarManager.get().terminate());
                    } else {
                        BananaToolbarManager.get().terminate();
                    }
                });
                put(R.id.visit_history, BananaToolbarManager.get()::goVisitHistory);
            }
        };
    }

    @Override
    public @NonNull Action getAction(@IdRes int id) {
        if (mActionMap.containsKey(id)) {
            return mActionMap.get(id);
        }
        return () -> {};
    }
}
