// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.quick_menu.QuickMenuViewModelData.ButtonInfo;

class QuickMenuControllerImpl implements QuickMenuController, QuickMenuView.Delegate {
    private final @NonNull QuickMenuViewModel<QuickMenuViewModelData> mQuickMenuViewModel;
    private final @NonNull QuickMenuActionProvider mQuickMenuActionProvider;

    QuickMenuControllerImpl(@NonNull QuickMenuViewModel<QuickMenuViewModelData> quickMenuViewModel,
            @NonNull QuickMenuActionProvider quickMenuActionProvider) {
        mQuickMenuViewModel = quickMenuViewModel;
        mQuickMenuActionProvider = quickMenuActionProvider;
        loadButtons();
    }

    private void loadButtons() {
        // FIXME(#958): Implement QuickMenuStorageModel
        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(R.id.add_secret_tab,
                R.drawable.ic_add_secret_tab_black_24dp, R.string.add_secret_tab));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(
                R.id.add_to_home, R.drawable.ic_add_to_home_black_24dp, R.string.add_to_home));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.archive, R.drawable.ic_archive_black_24dp, R.string.archive));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.at_me_game, R.drawable.atmegame_logo, R.string.at_me_game));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.back, R.drawable.btn_back, R.string.back));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(
                R.id.clear_data, R.drawable.ic_clear_browsing_data, R.string.clear_data));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(
                R.id.close_tab, R.drawable.ic_close_current_tab, R.string.close_tab));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.dark_mode, R.drawable.ic_dark_black_24dp, R.string.dark_mode));

        final boolean useDesktopView = BananaToolbarManager.get().getUseDesktopUserAgent();
        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(R.id.desktop_view,
                useDesktopView ? R.drawable.ic_phone_black_24dp : R.drawable.ic_desktop_black_24dp,
                useDesktopView ? R.string.mobile_view : R.string.desktop_view));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.download, R.drawable.ic_file_download_24dp, R.string.download));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(
                R.id.find_in_page, R.drawable.ic_find_in_page_black_24dp, R.string.find_in_page));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.forward, R.drawable.btn_forward, R.string.forward));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.new_tab, R.drawable.ic_add_black_24dp, R.string.new_tab));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(
                R.id.print, R.drawable.ic_local_printshop_black_24dp, R.string.print));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.qr_code, R.drawable.qr_code, R.string.qr_code));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.reload, R.drawable.ic_refresh_black_24dp, R.string.reload));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.search, R.drawable.ic_search_black_24dp, R.string.search));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.share, R.drawable.ic_share_black_24dp, R.string.share));

        mQuickMenuViewModel.getEditor().addButton(
                new ButtonInfo(R.id.terminate, R.drawable.ic_power_off_black, R.string.terminate));

        mQuickMenuViewModel.getEditor().addButton(new ButtonInfo(R.id.visit_history,
                R.drawable.ic_visit_history_black_24dp, R.string.visit_history));
    }

    @Override
    public void onClickQuickMenuButton(@IdRes int buttonId) {
        mQuickMenuActionProvider.getAction(buttonId).doAction();
    }

    @Override
    public void show() {
        mQuickMenuViewModel.getEditor().setIsShowing(true);
        mQuickMenuViewModel.commit();
    }

    @Override
    public void dismiss() {
        mQuickMenuViewModel.getEditor().setIsShowing(false);
        mQuickMenuViewModel.commit();
    }
}
