// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.view.View;

import org.triple.banana.R;
import org.triple.banana.public_api.export.BananaContextUtils;
import org.triple.banana.public_api.export.BananaToolsApi;

import org.chromium.ui.widget.Toast;

import java.util.EnumMap;

public enum ButtonId {
    BACK,
    FORWARD,
    SHARE,
    SEARCH,
    NEW_TAB,
    BOOKMARK,
    BOOKMARK_LIST,
    ADD_SECRET_TAB,
    DOWNLOAD,
    DESKTOP_VIEW,
    FIND_IN_PAGE,
    ADD_TO_HOME,
    RELOAD,
    VISIT_HISTORY,
    ARCHIVE,
    PRINT,
    SECRET_MODE,
    ADBLOCK,
    PASSWORD;

    static EnumMap<ButtonId, View.OnClickListener> sOnClickListeners =
            new EnumMap<>(ButtonId.class);

    static {
        sOnClickListeners.put(ButtonId.BACK, v -> BananaToolsApi.instance.back());
        sOnClickListeners.put(ButtonId.FORWARD, v -> BananaToolsApi.instance.forward());
        sOnClickListeners.put(ButtonId.SHARE, v -> BananaToolsApi.instance.share());
        sOnClickListeners.put(ButtonId.SEARCH, v -> BananaToolsApi.instance.search());
        sOnClickListeners.put(ButtonId.NEW_TAB, v -> BananaToolsApi.instance.addNewTab());
        sOnClickListeners.put(ButtonId.BOOKMARK, v -> BananaToolsApi.instance.addBookmark());
        sOnClickListeners.put(ButtonId.BOOKMARK_LIST, v -> BananaToolsApi.instance.goBookmark());
        sOnClickListeners.put(ButtonId.ADD_SECRET_TAB, v -> BananaToolsApi.instance.addSecretTab());
        sOnClickListeners.put(ButtonId.DOWNLOAD, v -> BananaToolsApi.instance.download());
        sOnClickListeners.put(
                ButtonId.DESKTOP_VIEW, v -> BananaToolsApi.instance.changeDesktopMode());
        sOnClickListeners.put(ButtonId.FIND_IN_PAGE, v -> BananaToolsApi.instance.findInPage());
        sOnClickListeners.put(ButtonId.ADD_TO_HOME, v -> BananaToolsApi.instance.addToHomeScreen());
        sOnClickListeners.put(ButtonId.RELOAD, v -> BananaToolsApi.instance.reload());
        sOnClickListeners.put(
                ButtonId.VISIT_HISTORY, v -> BananaToolsApi.instance.goVisitHistory());
        sOnClickListeners.put(ButtonId.ARCHIVE, v -> BananaToolsApi.instance.goArchive());
        sOnClickListeners.put(ButtonId.PASSWORD, v -> BananaToolsApi.instance.goPasswordSetting());
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
                imageResource = R.drawable.ic_menu_share_holo_light;
                break;
            case SEARCH:
                imageResource = R.drawable.ic_search;
                break;
            case NEW_TAB:
                imageResource = R.drawable.ic_add_black_24dp;
                break;
            case BOOKMARK:
                imageResource = R.drawable.ic_star_border_black_24dp;
                break;
            case BOOKMARK_LIST:
                imageResource = R.drawable.ic_folder_special_black_24dp;
                break;
            case ADBLOCK:
                imageResource = R.drawable.ic_remove_circle_outline_black_24dp;
                break;
            case DOWNLOAD:
                imageResource = R.drawable.ic_file_download_24dp;
                break;
            case DESKTOP_VIEW:
                imageResource = R.drawable.ic_personal_video_black_24dp;
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
            case PASSWORD:
                imageResource = R.drawable.ic_fingerprint_black_24dp;
                break;
            case ARCHIVE:
                imageResource = R.drawable.ic_archive_black_24dp;
                break;
            default:
                // SECRET_MODE, ADD_SECRET_TAB, ADD_TO_HOME, VISIT_HISTORY
                // TODO(codeimpl) : After adding all images, it should be removed.
                imageResource = R.drawable.ic_not_interested_black_24dp;
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
        View.OnClickListener listener = sOnClickListeners.get(id);

        if (listener == null) {
            listener = v
                    -> Toast.makeText(BananaContextUtils.getApplicationContext(),
                                    id.name() + " Clicked", Toast.LENGTH_SHORT)
                               .show();
        }

        return listener;
    }
}
