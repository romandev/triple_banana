// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import org.triple.banana.R;

class QuickMenuMiddleButton extends AppCompatButton {
    public QuickMenuMiddleButton(@NonNull Context context) {
        this(context, null);
    }

    public QuickMenuMiddleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    private void initialize() {
        setBackground(getResources().getDrawable(
                R.drawable.quick_menu_button_image_ripple, getContext().getTheme()));
        setCompoundDrawablePadding(
                getResources().getDimensionPixelSize(R.dimen.quick_menu_button_icon_padding));
        setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.quick_menu_button_text_size));
        setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        setTextColor(getResources().getColorStateList(R.color.quick_menu_text));
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left,
            @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        Drawable background = ResourcesCompat.getDrawable(getResources(),
                R.drawable.quick_menu_button_image_background, getContext().getTheme());
        LayerDrawable iconDrawable = new LayerDrawable(new Drawable[] {background, top});
        final int paddingSize =
                getResources().getDimensionPixelSize(R.dimen.quick_menu_button_background_padding);
        iconDrawable.setLayerInset(1, paddingSize, paddingSize, paddingSize, paddingSize);

        super.setCompoundDrawablesWithIntrinsicBounds(left, iconDrawable, right, bottom);
    }

    public void setIcon(@DrawableRes int icon) {
        setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
    }
}
