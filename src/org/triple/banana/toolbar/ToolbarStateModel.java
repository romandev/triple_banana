// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import java.util.ArrayList;

public enum ToolbarStateModel {
    instance;
    private ArrayList<ButtonId> mToolbarButtonList;
    private ArrayList<IToolbarStateChangedObserver> mToolbarStateChangedObservers;
    private IToolbarStatePersistentStore mToolbarStatePersistentStore;

    ToolbarStateModel() {
        mToolbarButtonList = new ArrayList<>();
        mToolbarStateChangedObservers = new ArrayList<>();
        mToolbarStatePersistentStore = new ToolbarStatePreferenceStoreImpl();
        mToolbarButtonList = mToolbarStatePersistentStore.loadStateFromPersistentStore();
    }

    public static ToolbarStateModel getInstance() {
        return instance;
    }

    public void addObserver(IToolbarStateChangedObserver observer) {
        mToolbarStateChangedObservers.add(observer);
    }

    public void removeObserver(IToolbarStateChangedObserver observer) {
        mToolbarStateChangedObservers.remove(observer);
    }

    public void notifyObservers() {
        for (IToolbarStateChangedObserver observer : mToolbarStateChangedObservers) {
            observer.onToolbarStateChanged(mToolbarButtonList);
        }
    }

    public ArrayList<ButtonId> getToolbarButtonList() {
        return mToolbarButtonList;
    }

    public void setToolbarButtonList(ArrayList<ButtonId> mToolbarButtonList) {
        this.mToolbarButtonList = mToolbarButtonList;
        notifyObservers();
    }

    public void commit() {
        mToolbarStatePersistentStore.storeStateToPersistentStore(mToolbarButtonList);
    }
}
