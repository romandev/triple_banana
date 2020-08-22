// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.remote_control;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.lang.ref.WeakReference;

public class RemoteControlGestureDetector implements View.OnTouchListener {
    final static String TAG = "RemoteControlGestureDetector";
    private GestureDetectorCompat mGestureDetectorCompat;
    private Callback mCallback;
    private WeakReference<View> mTargetView;

    public interface Callback {
        void onVolumeChanged(float value);
        void onBrightnessChanged(float value);
        void onPositionChanged(float value);
        void onControlsStateChanged();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetectorCompat.onTouchEvent(motionEvent);
    }

    public void stopDetection() {
        mCallback = null;
        if (mTargetView == null || mTargetView.get() == null) return;
        mTargetView.get().setOnTouchListener(null);
        mTargetView = null;
    }

    public void startDetection(View view, Callback callback) {
        mTargetView = new WeakReference<>(view);
        mTargetView.get().setOnTouchListener(this);
        mCallback = callback;
        mGestureDetectorCompat = createGestureDetectorCompat();
    }

    private GestureDetectorCompat createGestureDetectorCompat() {
        return new GestureDetectorCompat(BananaApplicationUtils.get().getApplicationContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onScroll(
                            MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        if (mTargetView == null || mTargetView.get() == null || mCallback == null)
                            return false;

                        // FIXME(#557): Fix sensitivity for gesture detector
                        final boolean isMoveHorizontal =
                                Math.abs(distanceX) + 1.0f > Math.abs(distanceY);

                        int width = mTargetView.get().getWidth();
                        int height = mTargetView.get().getHeight();

                        if (isMoveHorizontal) {
                            // FIXME(#557): Fix sensitivity for gesture detector
                            float value = 0.0f;
                            mCallback.onPositionChanged(value);
                        } else {
                            // FIXME(#557): Fix sensitivity for gesture detector
                            float value = distanceY * 0.001f;
                            if (width / 2 < e1.getX())
                                mCallback.onBrightnessChanged(value);
                            else
                                mCallback.onVolumeChanged(value);
                        }

                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // FIXME(#553): Implement forward and rewind when the user double-clicks the
                        // screen
                        return true;
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mCallback != null) {
                            mCallback.onControlsStateChanged();
                        }
                        return false;
                    }
                });
    }
}
