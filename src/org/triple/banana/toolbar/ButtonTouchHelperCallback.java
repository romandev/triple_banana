// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ButtonTouchHelperCallback extends ItemTouchHelper.Callback {
    OnItemMoveListener mOnItemMoveListener;

    public ButtonTouchHelperCallback(OnItemMoveListener mOnItemMoveListener) {
        this.mOnItemMoveListener = mOnItemMoveListener;
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
                | ItemTouchHelper.END;
        int swipeFlags = 0;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder viewHolder1) {
        mOnItemMoveListener.onItemMove(
                viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {}

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }
}
