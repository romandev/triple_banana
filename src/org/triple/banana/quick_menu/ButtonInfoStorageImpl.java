// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

import org.triple.banana.R;
import org.triple.banana.base.model.Model;

import java.util.ArrayList;
import java.util.List;

class ButtonInfoStorageImpl implements ButtonInfoStorage, Model.Data {
    private final @NonNull List<ButtonInfo> mButtons;

    ButtonInfoStorageImpl() {
        mButtons = new ArrayList<ButtonInfo>() {
            {
                add(new ButtonInfo(R.id.adblock,
                        R.drawable.adblock, R.string.adblock));
                add(new ButtonInfo(R.id.secure_dns,
                        R.drawable.secure_dns, R.string.secure_dns));
                add(new ButtonInfo(
                        R.id.archive, R.drawable.ic_archive_black_24dp, R.string.archive));
                add(new ButtonInfo(
                        R.id.visit_history, R.drawable.ic_visit_history_black_24dp, R.string.visit_history));
                add(new ButtonInfo(R.id.add_to_home,
                        R.drawable.ic_add_to_home_black_24dp, R.string.add_to_home));
                add(new ButtonInfo(
                        R.id.download, R.drawable.ic_download, R.string.download));
                add(new ButtonInfo(
                        R.id.dark_mode, R.drawable.ic_dark_black_24dp, R.string.dark_mode));
                add(new ButtonInfo(R.id.find_in_page,
                        R.drawable.ic_find_in_page_black_24dp, R.string.find_in_page));
                add(new ButtonInfo(
                        R.id.media_feature, R.drawable.media_feature, R.string.media_features));
                add(new ButtonInfo(
                        R.id.qr_code, R.drawable.qr_code, R.string.qr_code));
                add(new ButtonInfo(
                        R.id.share, R.drawable.ic_share_black_24dp, R.string.share));
                add(new ButtonInfo(R.id.desktop_view, R.drawable.ic_desktop_black_24dp,
                        R.string.desktop_view));
            }
        };
    }

    @Override
    public @NonNull List<ButtonInfo> getButtons() {
        return mButtons;
    }
}
