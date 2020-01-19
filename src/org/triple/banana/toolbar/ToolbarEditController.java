// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ToolbarEditController {
    private RecyclerView mSelectedRecyclerView;
    private RecyclerView mCandidateRecyclerView;

    static final String SELECTED_LABEL = "SELECTED";
    static final String CANDIDATE_LABEL = "CANDIDATE";

    public ToolbarEditController(
            RecyclerView selectedRecyclerView, RecyclerView candidateRecyclerView) {
        mSelectedRecyclerView = selectedRecyclerView;
        mCandidateRecyclerView = candidateRecyclerView;
    }

    public void commit() {
        ArrayList<ButtonId> idList = getButtonIdList(
                ((SelectedToolbarButtonAdapter) (mSelectedRecyclerView.getAdapter()))
                        .getButtonList());

        idList.addAll(getButtonIdList(
                ((CandidateToolbarButtonAdapter) (mCandidateRecyclerView.getAdapter()))
                        .getButtonList()));

        ToolbarStateModel.getInstance().setToolbarButtonList(idList);
        ToolbarStateModel.getInstance().commit();
    }

    private ArrayList<ButtonId> getButtonIdList(ArrayList<ToolbarButtonItem> buttonList) {
        ArrayList<ButtonId> idList = new ArrayList<>();

        for (int i = 0; i < buttonList.size(); i++) {
            ButtonId id = buttonList.get(i).getId();
            idList.add(id);
        }

        return idList;
    }

    void setButtonList(ArrayList<ButtonId> idList) {
        ArrayList<ToolbarButtonItem> selectedButtonList = new ArrayList<>();
        ArrayList<ToolbarButtonItem> unSelectedButtonList = new ArrayList<>();

        for (int i = 0; i < idList.size(); i++) {
            if (i > 5) {
                unSelectedButtonList.add(new ToolbarButtonItem(idList.get(i),
                        ButtonId.getImageResource(idList.get(i)), idList.get(i).name()));
            } else {
                selectedButtonList.add(new ToolbarButtonItem(idList.get(i),
                        ButtonId.getImageResource(idList.get(i)), idList.get(i).name()));
            }
        }

        SelectedToolbarButtonAdapter selectedButtonAdapter =
                new SelectedToolbarButtonAdapter(selectedButtonList);
        mSelectedRecyclerView.setAdapter(selectedButtonAdapter);

        CandidateToolbarButtonAdapter candidateToolbarButtonAdapter =
                new CandidateToolbarButtonAdapter(unSelectedButtonList);
        mCandidateRecyclerView.setAdapter(candidateToolbarButtonAdapter);
        mCandidateRecyclerView.addItemDecoration(new BottomToolbarButtonDecoration());

        selectedButtonAdapter.setCandidateToolbarButtonAdapter(candidateToolbarButtonAdapter);
        candidateToolbarButtonAdapter.setSelectedToolbarButtonAdapter(selectedButtonAdapter);
    }

    void initialization() {
        ArrayList<ToolbarButtonItem> selectedButtonList = new ArrayList<>();
        ArrayList<ToolbarButtonItem> candidateButtonList = new ArrayList<>();

        int i = 0;

        for (ButtonId id : ButtonId.values()) {
            if (i++ > 5) {
                candidateButtonList.add(
                        new ToolbarButtonItem(id, ButtonId.getImageResource(id), id.name()));
            } else {
                selectedButtonList.add(
                        new ToolbarButtonItem(id, ButtonId.getImageResource(id), id.name()));
            }
        }

        SelectedToolbarButtonAdapter selectedToolbarButtonAdapter =
                (SelectedToolbarButtonAdapter) mSelectedRecyclerView.getAdapter();
        selectedToolbarButtonAdapter.setButtonList(selectedButtonList);

        CandidateToolbarButtonAdapter bottomToolbarButtonAdapter1 =
                (CandidateToolbarButtonAdapter) mCandidateRecyclerView.getAdapter();
        bottomToolbarButtonAdapter1.setButtonList(candidateButtonList);
    }
}
