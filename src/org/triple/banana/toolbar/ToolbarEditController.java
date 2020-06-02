// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import androidx.recyclerview.widget.RecyclerView;

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
        ToolbarStateModel.getInstance().setToolbarButtonList(getButtonList());
        ToolbarStateModel.getInstance().commit();
    }

    private ArrayList<ButtonId> getButtonList() {
        ArrayList<ButtonId> idList = getButtonIdList(
                ((SelectedToolbarButtonAdapter) (mSelectedRecyclerView.getAdapter()))
                        .getButtonList());

        idList.addAll(getButtonIdList(
                ((CandidateToolbarButtonAdapter) (mCandidateRecyclerView.getAdapter()))
                        .getButtonList()));

        return idList;
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

    public boolean isToolbarChanged() {
        ArrayList<ButtonId> originList = ToolbarStateModel.getInstance().getToolbarButtonList();
        ArrayList<ButtonId> editedList = getButtonList();

        return !originList.equals(editedList);
    }
}
