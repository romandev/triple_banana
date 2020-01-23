// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.view.View;

import org.triple.banana.R;
import org.triple.banana.public_api.export.BananaContextUtils;

import org.chromium.ui.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public enum ButtonId {
    BACK,
    FORWARD,
    SHARE,
    SEARCH,
    NEW_TAB,
    BOOKMARK,
    BOOKMARK_LIST,
    ADD_BLOCK,
    SECRET_MODE,
    ADD_SECRET_TAB,
    DOWNLOAD,
    DESKTOP_VIEW,
    FIND_IN_PAGE,
    ADD_TO_HOME,
    RELOAD,
    VISIT_HISTORY,
    PRINT,
    PASSWORD,
    ARCHIVE;

    static Map<ButtonId, View.OnClickListener> sOnClickListeners;

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
            case ADD_BLOCK:
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

    public static void setOnClickListener(ButtonId id, View.OnClickListener clickListener) {
        if (sOnClickListeners == null) sOnClickListeners = new HashMap<>();

        sOnClickListeners.put(id, clickListener);
    }

    public static View.OnClickListener getOnClickListeners(ButtonId id) {
        if (sOnClickListeners == null) sOnClickListeners = new HashMap<>();

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
