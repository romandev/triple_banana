// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

public class SelectedToolbarButtonAdapter
        extends RecyclerView.Adapter<SelectedToolbarButtonAdapter.ButtonViewHolder> {
    private ArrayList<ToolbarButtonItem> mButtonList;
    private CandidateToolbarButtonAdapter mCandidateToolbarButtonAdapter;

    static class ReplaceItem {
        int mPosition;
        int mImageResource;
        ButtonId mButtonId;

        public ReplaceItem(int mPosition, int mImageResource, ButtonId mButtonId) {
            this.mPosition = mPosition;
            this.mImageResource = mImageResource;
            this.mButtonId = mButtonId;
        }

        public int getImageResource() {
            return mImageResource;
        }

        public void setImageResource(int mImageResource) {
            this.mImageResource = mImageResource;
        }

        public ButtonId getButtonId() {
            return mButtonId;
        }

        public void setButtonId(ButtonId mButtonId) {
            this.mButtonId = mButtonId;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int mPosition) {
            this.mPosition = mPosition;
        }
    }

    public SelectedToolbarButtonAdapter(ArrayList<ToolbarButtonItem> buttonList) {
        this.mButtonList = buttonList;
    }

    @Override
    public void onBindViewHolder(
            SelectedToolbarButtonAdapter.ButtonViewHolder buttonViewHolder, int i) {
        ToolbarButtonItem toolbarButtonItem = mButtonList.get(i);

        buttonViewHolder.mToolbarButton.setImageResource(toolbarButtonItem.getImageResource());
        buttonViewHolder.mToolbarButton.setToolbarButtonText(toolbarButtonItem.getName());
        ReplaceItem replaceItem =
                new ReplaceItem(i, toolbarButtonItem.getImageResource(), toolbarButtonItem.getId());
        buttonViewHolder.mToolbarButton.getToolbarWrapper().setTag(replaceItem);
        buttonViewHolder.mToolbarButton.getToolbarWrapper().setBackgroundColor(
                Color.rgb(255, 255, 255));
    }

    public void setCandidateToolbarButtonAdapter(
            CandidateToolbarButtonAdapter mCandidateToolbarButtonAdapter) {
        this.mCandidateToolbarButtonAdapter = mCandidateToolbarButtonAdapter;
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
                            new ClipData(ToolbarEditController.SELECTED_LABEL, mimeTypes, item);
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
                    v.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.parseColor("#FFFDC534"));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DROP:
                    if (!ToolbarEditController.SELECTED_LABEL.contentEquals(
                                event.getClipDescription().getLabel())) {
                        ArrayList<ToolbarButtonItem> lists =
                                mCandidateToolbarButtonAdapter.getButtonList();
                        ToolbarButtonItem button = lists.get(Integer.parseInt(
                                event.getClipData().getItemAt(0).getText().toString()));

                        int imageResource = button.getImageResource();
                        ButtonId buttonId = button.getId();
                        ReplaceItem item = (ReplaceItem) v.getTag();

                        button.setId(item.getButtonId());
                        button.setName(item.getButtonId().name());
                        button.setImageResource(item.getImageResource());

                        mButtonList.get(item.getPosition()).setId(buttonId);
                        mButtonList.get(item.getPosition()).setName(buttonId.name());
                        mButtonList.get(item.getPosition()).setImageResource(imageResource);

                        mCandidateToolbarButtonAdapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                    } else {
                        ReplaceItem item = (ReplaceItem) v.getTag();
                        Collections.swap(mButtonList,
                                Integer.parseInt(
                                        event.getClipData().getItemAt(0).getText().toString()),
                                item.getPosition());
                        notifyDataSetChanged();
                    }

                    break;
            }

            return true;
        });

        return buttonViewHolder;
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
}
