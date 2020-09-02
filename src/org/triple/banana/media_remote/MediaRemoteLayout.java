// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class MediaRemoteLayout extends RelativeLayout {
    Set<Listener> mListeners = new HashSet<>();

    public interface Listener {
        void onInterceptTouchEvent();
    }

    public MediaRemoteLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        for (Listener listener : mListeners) {
            listener.onInterceptTouchEvent();
        }

        return super.onInterceptTouchEvent(event);
    }
}
