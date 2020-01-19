// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

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
