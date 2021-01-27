// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class TerminateBrowser {
    private static TerminateBrowser sInstance;

    public static TerminateBrowser getInstance() {
        if (sInstance == null) {
            sInstance = new TerminateBrowser();
        }
        return sInstance;
    }

    public void terminate(@NonNull Context context) {
        showClearSuggestionDialog(context);
        // Boolean autoClear = ExtensionFeatures.isEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA);

        // if (autoClear) {
        //     BananaClearBrowsingData.get().clearBrowsingData(
        //             () -> BananaToolbarManager.get().terminate());
        // } else {
        //     BananaToolbarManager.get().terminate();
        // }
    }

    private void showClearSuggestionDialog(@NonNull Context context) {
        int length = context.getResources().getStringArray(R.array.clear_data_suggestion).length;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.terminate_browser));
        builder.setMultiChoiceItems(
                R.array.clear_data_suggestion, new boolean[length], (dialog, index, isChecked) -> {
                    android.util.Log.e(
                            "bk_1.ko", "clicked: " + index + " ,isChecked: " + isChecked);
                });
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
