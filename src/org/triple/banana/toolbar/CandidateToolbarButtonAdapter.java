// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.triple.banana.R;
import org.triple.banana.theme.DarkModeController;

import java.util.ArrayList;
import java.util.Collections;

public class CandidateToolbarButtonAdapter
        extends RecyclerView.Adapter<CandidateToolbarButtonAdapter.ButtonViewHolder> {
    private ArrayList<ToolbarButtonItem> mButtonList;
    private SelectedToolbarButtonAdapter mSelectedToolbarButtonAdapter;

    public CandidateToolbarButtonAdapter(ArrayList<ToolbarButtonItem> buttonList) {
        this.mButtonList = buttonList;
    }

    @Override
    public void onBindViewHolder(
            CandidateToolbarButtonAdapter.ButtonViewHolder buttonViewHolder, int i) {
        final Context context = BananaApplicationUtils.get().getApplicationContext();
        final boolean isDarkMode = DarkModeController.get().isDarkModeOn();

        ToolbarButtonItem toolbarButtonItem = mButtonList.get(i);

        int height = (int) TypedValue.applyDimension(
                (TypedValue.COMPLEX_UNIT_DIP), 80, context.getResources().getDisplayMetrics());

        buttonViewHolder.mToolbarButton.setImageResource(toolbarButtonItem.getImageResource());
        buttonViewHolder.mToolbarButton.setToolbarButtonText(toolbarButtonItem.getName());
        buttonViewHolder.mToolbarButton.getToolbarWrapper().setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        buttonViewHolder.mToolbarButton.getToolbarWrapper().setBackground(getDrawableForRect());
        buttonViewHolder.mToolbarButton.getImageButton().setColorFilter(
                isDarkMode ? Color.WHITE : Color.BLACK);
        buttonViewHolder.mToolbarButton.getTextView().setVisibility(View.VISIBLE);
        buttonViewHolder.mToolbarButton.getTextView().setTextColor(isDarkMode
                        ? Color.WHITE
                        : context.getResources().getColor(R.color.toolbar_modern_grey_text_color));

        SelectedToolbarButtonAdapter.ReplaceItem replaceItem =
                new SelectedToolbarButtonAdapter.ReplaceItem(
                        i, toolbarButtonItem.getImageResource(), toolbarButtonItem.getId());
        buttonViewHolder.mToolbarButton.getToolbarWrapper().setTag(replaceItem);
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ToolbarButton toolbarButton = new ToolbarButton(viewGroup.getContext());
        ButtonViewHolder buttonViewHolder = new ButtonViewHolder(toolbarButton);

        buttonViewHolder.mToolbarButton.getToolbarWrapper().setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ClipData.Item item = new ClipData.Item(
                            Integer.toString(buttonViewHolder.getAdapterPosition()));
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData dragData =
                            new ClipData(ToolbarEditController.CANDIDATE_LABEL, mimeTypes, item);
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

                    v.startDrag(dragData, myShadow, null, 0);
                    break;
                default:
                    break;
            }

            return false;
        });

        buttonViewHolder.mToolbarButton.getToolbarWrapper().setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENDED:
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(getDrawableForRect());
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.getBackground().setColorFilter(
                            Color.parseColor("#FFFDC534"), PorterDuff.Mode.SRC_IN);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DROP:
                    if (!ToolbarEditController.CANDIDATE_LABEL.contentEquals(
                                event.getClipDescription().getLabel())) {
                        ArrayList<ToolbarButtonItem> lists =
                                mSelectedToolbarButtonAdapter.getButtonList();
                        ToolbarButtonItem button = lists.get(Integer.parseInt(
                                event.getClipData().getItemAt(0).getText().toString()));

                        int imageResource = button.getImageResource();
                        ButtonId buttonId = button.getId();
                        SelectedToolbarButtonAdapter.ReplaceItem item =
                                (SelectedToolbarButtonAdapter.ReplaceItem) v.getTag();

                        button.setId(item.getButtonId());
                        button.setName(item.getButtonId().name());
                        button.setImageResource(item.getImageResource());

                        mButtonList.get(item.getPosition()).setId(buttonId);
                        mButtonList.get(item.getPosition()).setName(buttonId.name());
                        mButtonList.get(item.getPosition()).setImageResource(imageResource);

                        mSelectedToolbarButtonAdapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                    } else {
                        SelectedToolbarButtonAdapter.ReplaceItem item =
                                (SelectedToolbarButtonAdapter.ReplaceItem) v.getTag();

                        int selectedPosition = Integer.parseInt(
                                event.getClipData().getItemAt(0).getText().toString());
                        if (selectedPosition < 0 || selectedPosition >= mButtonList.size()) break;

                        Collections.swap(mButtonList, selectedPosition, item.getPosition());
                        notifyDataSetChanged();
                    }

                    break;
            }

            return true;
        });

        return buttonViewHolder;
    }

    public void setSelectedToolbarButtonAdapter(
            SelectedToolbarButtonAdapter mSelectedToolbarButtonAdapter) {
        this.mSelectedToolbarButtonAdapter = mSelectedToolbarButtonAdapter;
    }

    public void setButtonList(ArrayList<ToolbarButtonItem> mButtonList) {
        this.mButtonList = mButtonList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mButtonList == null) return 0;

        return mButtonList.size();
    }

    public ArrayList<ToolbarButtonItem> getButtonList() {
        return mButtonList;
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        ToolbarButton mToolbarButton;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);

            mToolbarButton = (ToolbarButton) itemView;
        }
    }

    private Drawable getDrawableForRect() {
        final Context context = BananaApplicationUtils.get().getApplicationContext();
        final boolean isDarkMode = DarkModeController.get().isDarkModeOn();
        Drawable roundRect = isDarkMode
                ? context.getResources().getDrawable(R.drawable.round_rect_dark)
                : context.getResources().getDrawable(R.drawable.round_rect);
        return roundRect;
    }
}
