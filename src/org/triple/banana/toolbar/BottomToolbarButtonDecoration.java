// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import org.banana.cake.interfaces.BananaApplicationUtils;

class BottomToolbarButtonDecoration extends RecyclerView.ItemDecoration {
    private int mSize;

    public BottomToolbarButtonDecoration() {
        mSize = dpToPx(2);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension((TypedValue.COMPLEX_UNIT_DIP), dp,
                BananaApplicationUtils.get()
                        .getApplicationContext()
                        .getResources()
                        .getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
            @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mSize;
        outRect.right = mSize;
        outRect.bottom = mSize;
        outRect.top = mSize;
    }
}
