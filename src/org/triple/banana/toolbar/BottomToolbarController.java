// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaBottomToolbarController;
import org.triple.banana.R;

// TODO(zino): We should remove this upstream dependency.
import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.ThemeColorProvider;
import org.chromium.chrome.browser.ThemeColorProvider.ThemeColorObserver;
import org.chromium.chrome.browser.compositor.layouts.OverviewModeBehavior;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.IncognitoStateProvider;
import org.chromium.chrome.browser.toolbar.IncognitoStateProvider.IncognitoStateObserver;
import org.chromium.chrome.browser.toolbar.MenuButton;
import org.chromium.chrome.browser.toolbar.TabCountProvider;
import org.chromium.chrome.browser.ui.appmenu.AppMenuButtonHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomToolbarController implements BananaBottomToolbarController,
                                                IToolbarStateChangedObserver, ThemeColorObserver,
                                                IncognitoStateObserver {
    private WeakReference<ViewGroup> mViewGroup;
    private static final int MAX_BUTTON_SIZE = 6;

    Map<ButtonId, ToolbarButton> mToolbarButtons;

    private ActivityTabProvider mTabProvider;
    private MenuButton mMenuButton;
    private OnClickListener mTabSwitcherListener;
    private AppMenuButtonHelper mMenuButtonHelper;
    private TabCountProvider mTabCountProvider;
    private ThemeColorProvider mThemeColorProvider;
    private OverviewModeBehavior mOverviewModeBehavior;
    private IncognitoStateProvider mIncognitoStateProvider;
    private boolean mIsInitializeWithNative;
    private boolean mIsIncognitoMode;

    @Override
    public BananaBottomToolbarController init(View root, ActivityTabProvider tabProvider) {
        mViewGroup = new WeakReference<>(root.findViewById(R.id.bottom_toolbar_browsing));

        mToolbarButtons = new HashMap<>();
        mTabProvider = tabProvider;

        mTabProvider.addObserverAndTrigger(new ActivityTabProvider.HintlessActivityTabObserver() {
            @Override
            public void onActivityTabChanged(Tab tab) {
                if (tab == null) return;
                tabProvider.removeObserver(this);
            }
        });

        ToolbarStateModel.getInstance().addObserver(this);
        ToolbarStateModel.getInstance().notifyObservers(); // only first time

        return this;
    }

    @Override
    public void onToolbarStateChanged(ArrayList<ButtonId> mButtonIdList) {
        ViewGroup viewGroup = mViewGroup.get();
        viewGroup.removeAllViews();

        buttonDestroy();

        for (int i = 0; i < MAX_BUTTON_SIZE; i++) {
            ToolbarButton toolbarButton =
                    new ToolbarButton(BananaApplicationUtils.get().getApplicationContext());
            int width = (int) TypedValue.applyDimension((TypedValue.COMPLEX_UNIT_DIP), 56,
                    BananaApplicationUtils.get()
                            .getApplicationContext()
                            .getResources()
                            .getDisplayMetrics());
            toolbarButton.setLayoutParams(
                    new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbarButton.setButtonId(mButtonIdList.get(i));
            toolbarButton.setImageResource(ButtonId.getImageResource(mButtonIdList.get(i)));
            if (ButtonId.getOnClickListeners(mButtonIdList.get(i)) != null) {
                toolbarButton.setOnClickListener(
                        ButtonId.getOnClickListeners(mButtonIdList.get(i)));
            }
            if (ButtonId.getOnClickListeners(mButtonIdList.get(i)) != null) {
                toolbarButton.setOnLongClickListener(
                        ButtonId.getOnLongClickListeners(mButtonIdList.get(i)));
            }
            toolbarButton.setActivityTabProvider(mTabProvider);
            viewGroup.addView(toolbarButton);
            mToolbarButtons.put(mButtonIdList.get(i), toolbarButton);
            if (mIsIncognitoMode) {
                toolbarButton.getImageButton().setImageTintList(
                        ColorStateList.valueOf(Color.WHITE));
            }

            if (i < MAX_BUTTON_SIZE - 1) addSpaceView();
        }

        makeToolbarMenuButton();

        if (mIsInitializeWithNative) {
            buttonInitializeWithNative();
        }
    }

    private void makeToolbarMenuButton() {
        View view = View.inflate(BananaApplicationUtils.get().getApplicationContext(),
                R.layout.toolbar_menu_button, null);
        mMenuButton = view.findViewById(R.id.menu_button_wrapper);
    }

    private void addSpaceView() {
        LayoutInflater inflater = (LayoutInflater) BananaApplicationUtils.get()
                                          .getApplicationContext()
                                          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar_space, mViewGroup.get(), true);
    }

    private void buttonInitializeWithNative() {
        for (ToolbarButton button : mToolbarButtons.values()) {
            button.setThemeColorProvider(mThemeColorProvider);
        }

        mThemeColorProvider.addThemeColorObserver(this);
        mIncognitoStateProvider.addIncognitoStateObserverAndTrigger(this);

        assert mMenuButtonHelper != null;
        mMenuButton.setAppMenuButtonHelper(mMenuButtonHelper);
        mMenuButton.setThemeColorProvider(mThemeColorProvider);
    }

    @Override
    public void initializeWithNative(OnClickListener tabSwitcherListener,
            AppMenuButtonHelper menuButtonHelper, OverviewModeBehavior overviewModeBehavior,
            TabCountProvider tabCountProvider, ThemeColorProvider themeColorProvider,
            IncognitoStateProvider incognitoStateProvider) {
        mIsInitializeWithNative = true;
        mTabSwitcherListener = tabSwitcherListener;
        mMenuButtonHelper = menuButtonHelper;
        mTabCountProvider = tabCountProvider;
        mThemeColorProvider = themeColorProvider;
        mOverviewModeBehavior = overviewModeBehavior;
        mIncognitoStateProvider = incognitoStateProvider;

        buttonInitializeWithNative();
    }

    @Override
    public void onThemeColorChanged(int primaryColor, boolean shouldAnimate) {
        mViewGroup.get().setBackgroundColor(primaryColor);
    }

    @Override
    public void onIncognitoStateChanged(boolean isIncognito) {
        mIsIncognitoMode = isIncognito;
    }

    @Override
    public void updateBookmarkButtonStatus(boolean isBookmarked, boolean editingAllowed) {
        ToolbarButton bookmarkButton = mToolbarButtons.get(ButtonId.BOOKMARK);
        if (bookmarkButton != null)
            bookmarkButton.updateBookmarkButtonState(isBookmarked, editingAllowed);
    }

    /**
     * Show the update badge over the bottom toolbar's app menu.
     */
    @Override
    public void showAppMenuUpdateBadge() {
        mMenuButton.showAppMenuUpdateBadgeIfAvailable(true);
    }

    /**
     * Remove the update badge.
     */
    @Override
    public void removeAppMenuUpdateBadge() {
        mMenuButton.removeAppMenuUpdateBadge(true);
    }

    /**
     * @return Whether the update badge is showing.
     */
    @Override
    public boolean isShowingAppMenuUpdateBadge() {
        return mMenuButton.isShowingAppMenuUpdateBadge();
    }

    /**
     * @return The browsing mode bottom toolbar's menu button.
     */
    @Override
    public MenuButton getMenuButton() {
        return mMenuButton;
    }

    /**
     * Clean up any state when the browsing mode bottom toolbar is destroyed.
     */
    private void buttonDestroy() {
        for (ToolbarButton toolbarButton : mToolbarButtons.values()) {
            toolbarButton.destroy();
        }

        mToolbarButtons.clear();

        if (mMenuButton != null) {
            mMenuButton.destroy();
            mMenuButton = null;
        }
    }

    @Override
    public void destroy() {
        buttonDestroy();

        if (mThemeColorProvider != null) {
            mThemeColorProvider.removeThemeColorObserver(this);
            mThemeColorProvider = null;
        }

        if (mIncognitoStateProvider != null) {
            mIncognitoStateProvider.removeObserver(this);
            mIncognitoStateProvider = null;
        }

        ToolbarStateModel.getInstance().removeObserver(this);
    }
}
