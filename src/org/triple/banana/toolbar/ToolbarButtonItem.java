// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

public class ToolbarButtonItem {
    private ButtonId mId;
    private int mImageResource;
    private String mName;

    public ToolbarButtonItem(ButtonId mId, int mImageResource) {
        this(mId, mImageResource, null);
    }

    public ToolbarButtonItem(ButtonId mId, int mImageResource, String mName) {
        this.mId = mId;
        this.mImageResource = mImageResource;
        this.mName = mName;
    }

    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getName() {
        return mName;
    }

    public void setId(ButtonId mId) {
        this.mId = mId;
    }

    public ButtonId getId() {
        return mId;
    }
}
