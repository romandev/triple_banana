// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.lang.ref.WeakReference;

public class MediaRemoteGestureDetector implements View.OnTouchListener {
    private static enum GestureType {
        UNKNOWN,
        VOLUME_CHANGED,
        BRIGHTNESS_CHANGED,
        POSITION_CHANGED
    }

    private final static String TAG = "MediaRemoteGestureDetector";
    private GestureDetectorCompat mGestureDetectorCompat;
    private Callback mCallback;
    private WeakReference<View> mTargetView;
    private GestureType mCurrentGesture;

    public interface Callback {
        void onVolumeChanged(float value);
        void onBrightnessChanged(float value);
        void onPositionChangeStart();
        void onPositionChange(float diff);
        void onPositionChangeFinish();
        void onControlsStateChanged();
        void onBackward();
        void onForward();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mGestureDetectorCompat.onTouchEvent(motionEvent)) {
            return true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            switch (mCurrentGesture) {
                case UNKNOWN:
                    break;
                case VOLUME_CHANGED:
                    mCallback.onVolumeChanged(0.0f /* isFinished */);
                    break;
                case BRIGHTNESS_CHANGED:
                    mCallback.onBrightnessChanged(0.0f /* isFinished */);
                    break;
                case POSITION_CHANGED:
                    mCallback.onPositionChangeFinish();
                    break;
            }
            mCurrentGesture = GestureType.UNKNOWN;
        }

        return false;
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
        mCurrentGesture = GestureType.UNKNOWN;
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

                        boolean isHorizontal = Math.abs(distanceX) > Math.abs(distanceY);
                        int width = mTargetView.get().getWidth();
                        int height = mTargetView.get().getHeight();

                        if (isHorizontal) {
                            if (Math.abs(distanceX) == 0.0f) return false;

                            if (mCurrentGesture == GestureType.UNKNOWN) {
                                mCurrentGesture = GestureType.POSITION_CHANGED;
                                mCallback.onPositionChangeStart();
                            }
                            if (mCurrentGesture == GestureType.POSITION_CHANGED) {
                                mCallback.onPositionChange(-distanceX / width);
                            }
                        } else {
                            if (Math.abs(distanceY) == 0.0f) return false;

                            if (width / 2 > e1.getX()) {
                                if (mCurrentGesture == GestureType.UNKNOWN) {
                                    mCurrentGesture = GestureType.BRIGHTNESS_CHANGED;
                                }
                                if (mCurrentGesture == GestureType.BRIGHTNESS_CHANGED) {
                                    mCallback.onBrightnessChanged(distanceY / height);
                                }
                            } else {
                                if (mCurrentGesture == GestureType.UNKNOWN) {
                                    mCurrentGesture = GestureType.VOLUME_CHANGED;
                                }
                                if (mCurrentGesture == GestureType.VOLUME_CHANGED) {
                                    mCallback.onVolumeChanged(distanceY / height);
                                }
                            }
                        }

                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (mTargetView == null || mTargetView.get() == null || mCallback == null) {
                            return false;
                        }

                        int width = mTargetView.get().getWidth();
                        if (width / 2 > e.getX()) {
                            mCallback.onBackward();
                        } else {
                            mCallback.onForward();
                        }

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
