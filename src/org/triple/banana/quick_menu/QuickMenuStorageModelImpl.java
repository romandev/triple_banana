// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;

import java.util.ArrayList;
import java.util.List;

class QuickMenuStorageModelImpl implements QuickMenuStorageModel<QuickMenuStorageModelData> {
    private final @NonNull List<QuickMenuStorageModelData> mDataList;

    QuickMenuStorageModelImpl() {
        mDataList = new ArrayList<QuickMenuStorageModelData>() {
            {
                add(new QuickMenuStorageModelData(R.id.add_secret_tab,
                        R.drawable.ic_add_secret_tab_black_24dp, R.string.add_secret_tab));
                add(new QuickMenuStorageModelData(R.id.add_to_home,
                        R.drawable.ic_add_to_home_black_24dp, R.string.add_to_home));
                add(new QuickMenuStorageModelData(
                        R.id.archive, R.drawable.ic_archive_black_24dp, R.string.archive));
                add(new QuickMenuStorageModelData(
                        R.id.at_me_game, R.drawable.atmegame_logo, R.string.at_me_game));
                add(new QuickMenuStorageModelData(R.id.back, R.drawable.btn_back, R.string.back));
                add(new QuickMenuStorageModelData(
                        R.id.clear_data, R.drawable.ic_clear_browsing_data, R.string.clear_data));
                add(new QuickMenuStorageModelData(
                        R.id.close_tab, R.drawable.ic_close_current_tab, R.string.close_tab));
                add(new QuickMenuStorageModelData(
                        R.id.dark_mode, R.drawable.ic_dark_black_24dp, R.string.dark_mode));
                final boolean useDesktopView = BananaToolbarManager.get().getUseDesktopUserAgent();
                add(new QuickMenuStorageModelData(R.id.desktop_view,
                        useDesktopView ? R.drawable.ic_phone_black_24dp
                                       : R.drawable.ic_desktop_black_24dp,
                        useDesktopView ? R.string.mobile_view : R.string.desktop_view));
                add(new QuickMenuStorageModelData(
                        R.id.download, R.drawable.ic_file_download_24dp, R.string.download));
                add(new QuickMenuStorageModelData(R.id.find_in_page,
                        R.drawable.ic_find_in_page_black_24dp, R.string.find_in_page));
                add(new QuickMenuStorageModelData(
                        R.id.forward, R.drawable.btn_forward, R.string.forward));
                add(new QuickMenuStorageModelData(
                        R.id.new_tab, R.drawable.ic_add_black_24dp, R.string.new_tab));
                add(new QuickMenuStorageModelData(
                        R.id.print, R.drawable.ic_local_printshop_black_24dp, R.string.print));
                add(new QuickMenuStorageModelData(
                        R.id.qr_code, R.drawable.qr_code, R.string.qr_code));
                add(new QuickMenuStorageModelData(
                        R.id.reload, R.drawable.ic_refresh_black_24dp, R.string.reload));
            }
        };
    }

    @Override
    public @NonNull List<QuickMenuStorageModelData> loadData() {
        return mDataList;
    }
}
