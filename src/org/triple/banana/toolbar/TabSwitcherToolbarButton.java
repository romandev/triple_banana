// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;

import org.chromium.chrome.browser.toolbar.TabSwitcherDrawable;

class TabSwitcherToolbarButton extends ToolbarButton {
    private TabSwitcherDrawable mTabSwitcherButtonDrawable;

    public TabSwitcherToolbarButton(Context context) {
        this(context, null);
    }

    public TabSwitcherToolbarButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSwitcherToolbarButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        mTabSwitcherButtonDrawable =
                TabSwitcherDrawable.createTabSwitcherDrawable(getContext(), false);
        getImageButton().setImageDrawable(mTabSwitcherButtonDrawable);
    }

    public void updateForTabCount(int tabCount, boolean isIncognito) {
        if (mTabSwitcherButtonDrawable != null) {
            mTabSwitcherButtonDrawable.updateForTabCount(tabCount, isIncognito);
        }
    }

    @Override
    public void onTintChanged(ColorStateList tint, boolean useLight) {
        super.onTintChanged(tint, useLight);
        if (mTabSwitcherButtonDrawable != null) {
            mTabSwitcherButtonDrawable.setTint(tint);
        }
    }
}
