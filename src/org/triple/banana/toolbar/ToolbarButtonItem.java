// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

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
