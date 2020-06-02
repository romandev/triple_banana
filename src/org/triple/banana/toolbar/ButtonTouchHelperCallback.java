// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
