// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnLongClickListener;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class LongClickablePreference extends Preference {
    private @Nullable OnLongClickListener mLongClickListener;

    public LongClickablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnLongClickListener(@Nullable OnLongClickListener listener) {
        mLongClickListener = listener;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setOnLongClickListener(mLongClickListener);
    }
}
