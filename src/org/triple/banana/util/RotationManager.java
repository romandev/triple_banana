// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.Surface;
import android.view.WindowManager;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.util.HashSet;

public class RotationManager implements DisplayManager.DisplayListener {
    private static final String TAG = "RotationManager";

    public static enum Rotation {
        UNKNOWN,
        R0,
        R90,
        R180,
        R270,
    }

    public static enum Orientation {
        UNKNOWN,
        LANDSCAPE,
        PORTRAIT,
    }

    public static class Info {
        public final Rotation rotation;
        public final Orientation orientation;
        public static final Info ROTATION_0 = new Info(Surface.ROTATION_0);
        public static final Info ROTATION_90 = new Info(Surface.ROTATION_90);
        public static final Info ROTATION_180 = new Info(Surface.ROTATION_180);
        public static final Info ROTATION_270 = new Info(Surface.ROTATION_270);

        private Info(int rotation) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    this.rotation = Rotation.R0;
                    this.orientation = Orientation.PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    this.rotation = Rotation.R90;
                    this.orientation = Orientation.LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    this.rotation = Rotation.R180;
                    this.orientation = Orientation.PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    this.rotation = Rotation.R270;
                    this.orientation = Orientation.LANDSCAPE;
                    break;
                default:
                    this.rotation = Rotation.UNKNOWN;
                    this.orientation = Orientation.UNKNOWN;
                    break;
            }
        }
    }

    public interface Listener {
        void onRotateChanged(Info rotationInfo);
    }

    private int mLastRotation;
    private HashSet<Listener> mListeners = new HashSet<>();

    public RotationManager() {
        mLastRotation = getRotation();

        Context context = BananaApplicationUtils.get().getApplicationContext();
        DisplayManager displayManager =
                (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(this, new Handler());
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
        listener.onRotateChanged(new Info(mLastRotation));
    }

    public void removeListener(Listener listener) {
        if (mListeners.contains(listener)) mListeners.remove(listener);
    }

    @Override
    public void onDisplayAdded(int displayId) {}

    @Override
    public void onDisplayChanged(int displayId) {
        int rotation = getRotation();
        if (rotation == mLastRotation) return;

        mLastRotation = rotation;

        Info rotationInfo = new Info(rotation);
        for (Listener listener : mListeners) {
            listener.onRotateChanged(rotationInfo);
        }
    }

    @Override
    public void onDisplayRemoved(int displayId) {}

    private int getRotation() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getRotation();
    }
}
