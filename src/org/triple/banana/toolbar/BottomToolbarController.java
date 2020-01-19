// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.triple.banana.R;
// TODO(zino): We should remove this upstream dependency.
import org.triple.banana.public_api.export.BananaContextUtils;

import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.ThemeColorProvider;
import org.chromium.chrome.browser.appmenu.AppMenuButtonHelper;
import org.chromium.chrome.browser.compositor.layouts.OverviewModeBehavior;
import org.chromium.chrome.browser.ntp.NewTabPage;
import org.chromium.chrome.browser.partnercustomizations.HomepageManager;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.tab.TabThemeColorHelper;
import org.chromium.chrome.browser.toolbar.IncognitoStateProvider;
import org.chromium.chrome.browser.toolbar.MenuButton;
import org.chromium.chrome.browser.toolbar.TabCountProvider;
import org.chromium.chrome.browser.toolbar.bottom.BottomToolbarCoordinator.BottomToolbarCoordinatorDelegate;
import org.chromium.ui.modelutil.PropertyModelChangeProcessor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BottomToolbarController
        implements BottomToolbarCoordinatorDelegate, IToolbarStateChangedObserver {
    private final WeakReference<ViewGroup> mViewGroup;
    private static final int MAX_BUTTON_SIZE = 6;

    ArrayList<ToolbarButton> mToolbarButtons;

    private ActivityTabProvider mTabProvider;
    private MenuButton mMenuButton;
    private OnClickListener mTabSwitcherListener;
    private AppMenuButtonHelper mMenuButtonHelper;
    private TabCountProvider mTabCountProvider;
    private ThemeColorProvider mThemeColorProvider;
    private OverviewModeBehavior mOverviewModeBehavior;
    private boolean mIsInitializeWithNative;

    private static BottomToolbarController sInstance;

    private BottomToolbarController(View root, ActivityTabProvider tabProvider) {
        mViewGroup = new WeakReference<>(root.findViewById(R.id.bottom_toolbar_browsing));

        mToolbarButtons = new ArrayList<>();
        mTabProvider = tabProvider;

        mTabProvider.addObserverAndTrigger(new ActivityTabProvider.HintlessActivityTabObserver() {
            @Override
            public void onActivityTabChanged(Tab tab) {
                if (tab == null) return;
                tabProvider.removeObserver(this);
            }
        });
    }

    public static void createBottomToolbarController(View root, ActivityTabProvider tabProvider) {
        if (sInstance == null) sInstance = new BottomToolbarController(root, tabProvider);

        ToolbarStateModel.getInstance().addObserver(sInstance);
        ToolbarStateModel.getInstance().notifyObservers(); // only first time
    }

    public static BottomToolbarController getInstance() {
        return sInstance;
    }

    @Override
    public void onToolbarStateChanged(ArrayList<ButtonId> mButtonIdList) {
        ViewGroup viewGroup = mViewGroup.get();
        viewGroup.removeAllViews();

        buttonDestroy();

        for (int i = 0; i < MAX_BUTTON_SIZE; i++) {
            ToolbarButton toolbarButton =
                    new ToolbarButton(BananaContextUtils.getApplicationContext());
            int width = (int) TypedValue.applyDimension((TypedValue.COMPLEX_UNIT_DIP), 56,
                    BananaContextUtils.getApplicationContext().getResources().getDisplayMetrics());
            toolbarButton.setLayoutParams(
                    new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbarButton.setButtonId(mButtonIdList.get(i));
            toolbarButton.setImageResource(ButtonId.getImageResource(mButtonIdList.get(i)));
            toolbarButton.setOnClickListener(ButtonId.getOnClickListeners(mButtonIdList.get(i)));
            toolbarButton.setActivityTabProvider(mTabProvider);
            viewGroup.addView(toolbarButton);
            mToolbarButtons.add(toolbarButton);

            if (i < MAX_BUTTON_SIZE - 1) addSpaceView();
        }

        makeToolbarMenuButton();

        if (mIsInitializeWithNative) {
            buttonInitializeWithNative();
        }
    }

    private void makeToolbarMenuButton() {
        View view = View.inflate(
                BananaContextUtils.getApplicationContext(), R.layout.toolbar_menu_button, null);
        mMenuButton = view.findViewById(R.id.menu_button_wrapper);
        mMenuButton.setWrapperView(view.findViewById(R.id.labeled_menu_button_wrapper));
    }

    private void addSpaceView() {
        LayoutInflater inflater =
                (LayoutInflater) BananaContextUtils.getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar_space, mViewGroup.get(), true);
    }

    private void buttonInitializeWithNative() {
        for (ToolbarButton button : mToolbarButtons) {
            button.setThemeColorProvider(mThemeColorProvider);
        }

        assert mMenuButtonHelper != null;
        mMenuButton.setAppMenuButtonHelper(mMenuButtonHelper);
        mMenuButton.setThemeColorProvider(mThemeColorProvider);

        TabThemeColorHelper.get(mTabProvider.get()).updateIfNeeded(false);
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

        buttonInitializeWithNative();
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
        for (ToolbarButton toolbarButton : mToolbarButtons) {
            toolbarButton.destroy();
        }

        mToolbarButtons.clear();

        if (mMenuButton != null) {
            mMenuButton.destroy();
            mMenuButton = null;
        }
    }

    private ToolbarButton.ButtonState getButtonState(ButtonId id) {
        if (id == ButtonId.HOME) {
            return new ToolbarButton.ButtonState() {
                @Override
                public Boolean onObservingDifferentTab(Tab tab) {
                    return updateHomeButtonEnabledState(tab, null);
                }

                @Override
                public Boolean onUpdateUrl(Tab tab, String url) {
                    return updateHomeButtonEnabledState(tab, url);
                }
            };
        }
        return null;
    }

    private Boolean updateHomeButtonEnabledState(Tab tab, String url) {
        // New tab page button takes precedence over homepage.
        final boolean isHomepageEnabled = HomepageManager.isHomepageEnabled();
        final boolean isEnabled = !isActiveTabNTP()
                || (isHomepageEnabled && !NewTabPage.isNTPUrl(HomepageManager.getHomepageUri()));
        return isEnabled;
    }

    private boolean isActiveTabNTP() {
        if (mTabProvider == null) return false;

        final Tab tab = mTabProvider.get();
        if (tab == null) return false;

        return NewTabPage.isNTPUrl(tab.getUrl());
    }

    @Override
    public void destroy() {
        buttonDestroy();
        ToolbarStateModel.getInstance().removeObserver(sInstance);
        sInstance = null;
    }
}
