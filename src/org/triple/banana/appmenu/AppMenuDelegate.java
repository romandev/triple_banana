// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.appmenu;

import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import org.banana.cake.interfaces.BananaAppMenu;
import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;

import java.lang.ref.WeakReference;

public class AppMenuDelegate implements BananaAppMenu {
    private static final String NEW_FEATURE_ICON = "new_feature_icon";
    private WeakReference<ImageView> mNewFeatureIcon;

    public static AppMenuDelegate get() {
        return (AppMenuDelegate) BananaAppMenu.get();
    }

    @Override
    public void prepareMenu(Menu menu) {
        if (menu.findItem(R.id.banana_extension) == null) return;

        if (shouldShowNewFeatureIcon()) {
            menu.findItem(R.id.banana_extension).setIcon(R.drawable.ic_new);
        } else {
            menu.findItem(R.id.banana_extension).setIcon(null);
        }
    }

    @Override
    public void prepareMenuButton(FrameLayout menuButton) {
        if (menuButton.findViewWithTag(NEW_FEATURE_ICON) != null) return;
        if (mNewFeatureIcon != null && mNewFeatureIcon.get() != null) return;

        ImageView newFeatureIcon = new ImageView(
                new ContextThemeWrapper(menuButton.getContext(), R.style.ToolbarMenuButtonPhone));
        newFeatureIcon.setTag(NEW_FEATURE_ICON);
        newFeatureIcon.setImageResource(R.drawable.ic_new);
        newFeatureIcon.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        newFeatureIcon.setVisibility(shouldShowNewFeatureIcon() ? View.VISIBLE : View.GONE);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        newFeatureIcon.setLayoutParams(params);

        menuButton.addView(newFeatureIcon);

        mNewFeatureIcon = new WeakReference<>(newFeatureIcon);
    }

    private boolean shouldShowNewFeatureIcon() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                "should_show_new_feature_icon", false);
    }

    public void setNewFeatureIcon(boolean value) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean("should_show_new_feature_icon", value);
        editor.apply();

        if (mNewFeatureIcon == null || mNewFeatureIcon.get() == null) return;
        mNewFeatureIcon.setVisibility(value ? View.VISIBLE : View.GONE);
    }
}
