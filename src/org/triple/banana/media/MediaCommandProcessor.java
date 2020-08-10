// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media;

import org.banana.cake.interfaces.BananaTab;

public enum MediaCommandProcessor {
    instance;

    private void runMediaCommand(String mediaCommand) {
        BananaTab tab = org.banana.cake.interfaces.BananaTabManager.get().getActivityTab();
        if (tab == null) return;

        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("document.fullscreenElement.");
        commandBuilder.append(mediaCommand);
        tab.evaluateJavaScript(commandBuilder.toString());
    }

    public void play() {
        runMediaCommand("play()");
    }

    public void pause() {
        runMediaCommand("pause()");
    }

    public void setPosition(float position) {
        runMediaCommand("currentTime = " + Float.toString(position));
    }

    public void setRelativePosition(float position) {
        runMediaCommand("currentTime += " + Float.toString(position));
    }

    public void setVolume(float value) {
        if (value < 0.0f) {
            value = 0.0f;
        } else if (value > 1.0f) {
            value = 1.0f;
        }

        runMediaCommand("volume = " + Float.toString(value));
    }

    public void setRelativeVolume(float value) {
        runMediaCommand("volume = Math.min(1.0, Math.max(0.0, document.fullscreenElement.volume + "
                + Float.toString(value) + "))");
    }
}
