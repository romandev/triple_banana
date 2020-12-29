// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.quick_menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import org.triple.banana.R;

class QuickMenuButton extends RelativeLayout {
    private @NonNull ImageButton mImageButton;
    private @NonNull TextView mTextView;

    public QuickMenuButton(@NonNull Context context) {
        this(context, null);
    }

    public QuickMenuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    // FIXME(#960): Relativelayout.LayoutParams codes need to move to quick_menu_button.xml
    private void initialize(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.quick_menu_button, this, true);

        mImageButton = findViewById(R.id.quick_menu_button_image);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.quick_menu_button_image_length),
                getResources().getDimensionPixelSize(R.dimen.quick_menu_button_image_length));
        imageButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL, getId());
        mImageButton.setLayoutParams(imageButtonParams);

        mTextView = findViewById(R.id.quick_menu_button_text);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, getId());
        textViewParams.addRule(RelativeLayout.BELOW, mImageButton.getId());
        mTextView.setLayoutParams(textViewParams);
    }

    public void setLabel(@StringRes int resId) {
        mTextView.setText(resId);
    }

    public void setImageResource(@DrawableRes int resId) {
        mImageButton.setImageResource(resId);
    }

    public void setImageVisible(boolean visible) {
        mImageButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setLabelVisible(boolean visible) {
        mTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
