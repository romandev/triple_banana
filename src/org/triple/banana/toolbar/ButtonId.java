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
    HOME,
    SHARE,
    SEARCH,
    FORWARD,
    CAPTURE,
    NEW_PAGE,
    NIGHT_MODE,
    FAVORITE,
    ONLY_IMAGE,
    LANGUAGE,
    TBA,
    POWER,
    BLOCK,
    SETTING,
    SHOPPING,
    STAR,
    TRANSLATE,
    YOUTUBE,
    VOLUME_OFF,
    BRIGHTNESS,
    BOYSANDGIRS,
    PC,
    BACK,
    DOWN;

    static Map<ButtonId, View.OnClickListener> sOnClickListeners;

    public static int getImageResource(ButtonId id) {
        int imageResource = 0;
        switch (id) {
            case HOME:
                imageResource = R.drawable.btn_toolbar_home;
                break;
            case SHARE:
                imageResource = R.drawable.ic_menu_share_holo_light;
                break;
            case SEARCH:
                imageResource = R.drawable.ic_search;
                break;
            case FORWARD:
                imageResource = R.drawable.btn_forward;
                break;
            case BACK:
                imageResource = R.drawable.btn_back;
                break;
            case DOWN:
                imageResource = R.drawable.ic_file_download_24dp;
                break;
            case CAPTURE:
                imageResource = R.drawable.ic_add_a_photo_black_24dp;
                break;
            case NEW_PAGE:
                imageResource = R.drawable.ic_add_black_24dp;
                break;
            case NIGHT_MODE:
                imageResource = R.drawable.ic_brightness_3_black_24dp;
                break;
            case FAVORITE:
                imageResource = R.drawable.ic_favorite_border_black_24dp;
                break;
            case ONLY_IMAGE:
                imageResource = R.drawable.ic_filter_black_24dp;
                break;
            case LANGUAGE:
                imageResource = R.drawable.ic_language_black_24dp;
                break;
            case TBA:
                imageResource = R.drawable.ic_merge_type_black_24dp;
                break;
            case POWER:
                imageResource = R.drawable.ic_power_settings_new_black_24dp;
                break;
            case BLOCK:
                imageResource = R.drawable.ic_remove_circle_outline_black_24dp;
                break;
            case SETTING:
                imageResource = R.drawable.ic_settings_black_24dp;
                break;
            case SHOPPING:
                imageResource = R.drawable.ic_shopping_cart_black_24dp;
                break;
            case STAR:
                imageResource = R.drawable.ic_star_border_black_24dp;
                break;
            case TRANSLATE:
                imageResource = R.drawable.ic_translate_black_24dp;
                break;
            case YOUTUBE:
                imageResource = R.drawable.ic_video_library_black_24dp;
                break;
            case VOLUME_OFF:
                imageResource = R.drawable.ic_volume_off_black_24dp;
                break;
            case BRIGHTNESS:
                imageResource = R.drawable.ic_wb_sunny_black_24dp;
                break;
            case BOYSANDGIRS:
                imageResource = R.drawable.ic_wc_black_24dp;
                break;
            case PC:
                imageResource = R.drawable.ic_personal_video_black_24dp;
                break;
            default:
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
