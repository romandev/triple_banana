// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;

import org.chromium.base.ApiCompatibilityUtils;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.ThemeColorProvider;
import org.chromium.chrome.browser.toolbar.ThemeColorProvider.TintObserver;
import org.chromium.ui.widget.ChromeImageButton;

/**
 * The Toolbar button.
 */
class ToolbarButton extends LinearLayout implements TintObserver {
    /**
     * A provider that notifies components when the theme color changes.
     */

    private ButtonId mButtonId;
    private ThemeColorProvider mThemeColorProvider;

    private LinearLayout mToolbarWrapper;
    private ChromeImageButton mImageButton;
    private TextView mTextView;

    public ToolbarButton(Context context) {
        this(context, null);
    }

    public ToolbarButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setButtonId(ButtonId buttonId) {
        this.mButtonId = buttonId;
    }

    public ButtonId getButtonId() {
        return mButtonId;
    }

    private void init() {
        LayoutInflater.from(BananaApplicationUtils.get().getApplicationContext())
                .inflate(R.layout.toolbar_button, this, true);

        mToolbarWrapper = findViewById(R.id.toolbar_button_wrapper);
        mImageButton = findViewById(R.id.image_button);
        mTextView = findViewById(R.id.button_label);
    }

    public LinearLayout getToolbarWrapper() {
        return mToolbarWrapper;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setToolbarButtonText(String str) {
        mTextView.setText(str);
    }

    public void setImageResource(@DrawableRes int resId) {
        mImageButton.setImageResource(resId);
    }

    public void setImageColor(int color) {
        mImageButton.setColorFilter(color);
    }

    public ChromeImageButton getImageButton() {
        return mImageButton;
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setThemeColorProvider(ThemeColorProvider themeColorProvider) {
        mThemeColorProvider = themeColorProvider;
        mThemeColorProvider.addTintObserver(this);
    }

    public void updateBookmarkButtonState(boolean isBookmarked, boolean editingAllowed) {
        if (mButtonId == ButtonId.BOOKMARK) {
            if (mThemeColorProvider == null) return;
            if (isBookmarked) {
                mImageButton.setImageResource(R.drawable.btn_star_filled);
                ApiCompatibilityUtils.setImageTintList(getImageButton(),
                        mThemeColorProvider.useLight()
                                ? mThemeColorProvider.getTint()
                                : AppCompatResources.getColorStateList(
                                        BananaApplicationUtils.get().getApplicationContext(),
                                        R.color.blue_mode_tint));
            } else {
                mImageButton.setImageResource(R.drawable.btn_star);
                ApiCompatibilityUtils.setImageTintList(
                        getImageButton(), mThemeColorProvider.getTint());
            }
            setEnabled(editingAllowed);
        }
    }

    public void updateBackButtonVisibility(boolean canGoBack) {
        if (mButtonId == ButtonId.BACK) {
            mImageButton.setEnabled(canGoBack);
            setEnabled(canGoBack);
        }
    }

    public void updateForwardButtonVisibility(boolean canGoForward) {
        if (mButtonId == ButtonId.FORWARD) {
            mImageButton.setEnabled(canGoForward);
            setEnabled(canGoForward);
        }
    }

    public void updateDesktopViewButtonState(boolean isDesktopView) {
        if (mButtonId == ButtonId.DESKTOP_VIEW) {
            setImageResource(isDesktopView ? R.drawable.ic_phone_black_24dp
                                           : R.drawable.ic_desktop_black_24dp);
        }
    }

    @Override
    public void onTintChanged(ColorStateList tint, boolean useLight) {
        ApiCompatibilityUtils.setImageTintList(mImageButton, tint);
    }

    public void destroy() {
        if (mThemeColorProvider != null) {
            mThemeColorProvider.removeTintObserver(this);
            mThemeColorProvider = null;
        }
    }
}
