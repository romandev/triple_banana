// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.view.View;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.theme.DarkModeController;

import java.util.EnumMap;

public enum ButtonId {
    FORWARD,
    SHARE,
    BOOKMARK,
    SEARCH,
    NEW_TAB,
    BANANA_EXTENSION,
    BACK,
    ADD_SECRET_TAB,
    DOWNLOAD,
    DESKTOP_VIEW,
    FIND_IN_PAGE,
    ADD_TO_HOME,
    RELOAD,
    VISIT_HISTORY,
    ARCHIVE,
    PRINT,
    DARK_MODE,
    TERMINATE,
    AT_ME_GAME;

    static EnumMap<ButtonId, View.OnClickListener> sOnClickListeners =
            new EnumMap<>(ButtonId.class);
    static EnumMap<ButtonId, View.OnLongClickListener> sOnLongClickListeners =
            new EnumMap<>(ButtonId.class);

    static {
        sOnClickListeners.put(ButtonId.BACK, v -> BananaToolbarManager.get().back());
        sOnClickListeners.put(ButtonId.FORWARD, v -> BananaToolbarManager.get().forward());
        sOnClickListeners.put(ButtonId.SHARE, v -> BananaToolbarManager.get().share());
        sOnClickListeners.put(ButtonId.SEARCH, v -> BananaToolbarManager.get().search());
        sOnClickListeners.put(ButtonId.NEW_TAB, v -> BananaToolbarManager.get().addNewTab());
        sOnClickListeners.put(ButtonId.BANANA_EXTENSION,
                v -> BananaToolbarManager.get().openSettingPage(ExtensionFeatures.class));
        sOnClickListeners.put(ButtonId.BOOKMARK, v -> BananaToolbarManager.get().goBookmark());
        sOnClickListeners.put(
                ButtonId.ADD_SECRET_TAB, v -> BananaToolbarManager.get().addSecretTab());
        sOnClickListeners.put(ButtonId.DOWNLOAD, v -> BananaToolbarManager.get().download());
        sOnClickListeners.put(
                ButtonId.DESKTOP_VIEW, v -> BananaToolbarManager.get().changeDesktopMode());
        sOnClickListeners.put(ButtonId.FIND_IN_PAGE, v -> BananaToolbarManager.get().findInPage());
        sOnClickListeners.put(
                ButtonId.ADD_TO_HOME, v -> BananaToolbarManager.get().addToHomeScreen());
        sOnClickListeners.put(ButtonId.RELOAD, v -> BananaToolbarManager.get().reload());
        sOnClickListeners.put(
                ButtonId.VISIT_HISTORY, v -> BananaToolbarManager.get().goVisitHistory());
        sOnClickListeners.put(ButtonId.ARCHIVE, v -> BananaToolbarManager.get().goArchive());
        sOnClickListeners.put(ButtonId.PRINT, v -> BananaToolbarManager.get().print());
        sOnClickListeners.put(ButtonId.DARK_MODE, v -> DarkModeController.get().toggle());

        sOnLongClickListeners.put(ButtonId.BOOKMARK, v -> {
            BananaToolbarManager.get().addBookmark();
            return true;
        });

        sOnClickListeners.put(ButtonId.TERMINATE, v -> BananaToolbarManager.get().terminate());
        sOnClickListeners.put(ButtonId.AT_ME_GAME,
                v
                -> BananaApplicationUtils.get().showInfoPage(
                        "https://www.atmegame.com/?utm_source=Banana&utm_medium=Banana"));
    }

    public static int getImageResource(ButtonId id) {
        int imageResource = 0;
        switch (id) {
            case BACK:
                imageResource = R.drawable.btn_back;
                break;
            case FORWARD:
                imageResource = R.drawable.btn_forward;
                break;
            case SHARE:
                imageResource = R.drawable.ic_share_black_24dp;
                break;
            case SEARCH:
                imageResource = R.drawable.ic_search_black_24dp;
                break;
            case NEW_TAB:
                imageResource = R.drawable.ic_add_black_24dp;
                break;
            case BOOKMARK:
                imageResource = R.drawable.ic_star_border_black_24dp;
                break;
            case DOWNLOAD:
                imageResource = R.drawable.ic_file_download_24dp;
                break;
            case DESKTOP_VIEW:
                imageResource = R.drawable.ic_desktop_black_24dp;
                break;
            case RELOAD:
                imageResource = R.drawable.ic_refresh_black_24dp;
                break;
            case FIND_IN_PAGE:
                imageResource = R.drawable.ic_find_in_page_black_24dp;
                break;
            case PRINT:
                imageResource = R.drawable.ic_local_printshop_black_24dp;
                break;
            case ARCHIVE:
                imageResource = R.drawable.ic_archive_black_24dp;
                break;
            case BANANA_EXTENSION:
                imageResource = R.drawable.toolbar_button_banana;
                break;
            case ADD_SECRET_TAB:
                imageResource = R.drawable.ic_add_secret_tab_black_24dp;
                break;
            case VISIT_HISTORY:
                imageResource = R.drawable.ic_visit_history_black_24dp;
                break;
            case ADD_TO_HOME:
                imageResource = R.drawable.ic_add_to_home_black_24dp;
                break;
            case DARK_MODE:
                imageResource = R.drawable.ic_dark_black_24dp;
                break;
            case TERMINATE:
                imageResource = R.drawable.ic_power_off_black;
                break;
            case AT_ME_GAME:
                imageResource = R.drawable.atmegame_logo;
                break;
        }
        return imageResource;
    }

    public static ButtonId getButtonId(String string) {
        for (ButtonId id : ButtonId.values()) {
            if (id.name().equals(string)) return id;
        }
        return null;
    }

    public static View.OnClickListener getOnClickListeners(ButtonId id) {
        return sOnClickListeners.get(id);
    }

    public static View.OnLongClickListener getOnLongClickListeners(ButtonId id) {
        return sOnLongClickListeners.get(id);
    }
}
