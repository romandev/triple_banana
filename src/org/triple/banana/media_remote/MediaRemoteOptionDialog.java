// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.media_remote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.banana.cake.interfaces.BananaApplicationUtils;

class MediaRemoteOptionDialog {
    private @Nullable AlertDialog mAlertDialog;

    static interface DataModel<T> {
        @NonNull
        CharSequence[] getLabels();
        int getSelectedIndex();
        @NonNull
        T getSelectedData(int index);
    }

    static interface Callback<T> { void onResult(@Nullable T item); }

    MediaRemoteOptionDialog(){}

    <T> void show(@NonNull Context context, final @NonNull DataModel<T> model,
            final @NonNull Callback<T> callback) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            return;
        }
        mAlertDialog = BananaApplicationUtils.get()
                               .getDialogBuilder(context)
                               .setNegativeButton(android.R.string.cancel, null)
                               .setSingleChoiceItems(model.getLabels(), model.getSelectedIndex(),
                                       (dialog, which) -> {
                                           callback.onResult(model.getSelectedData(which));
                                           dialog.dismiss();
                                       })
                               .create();
        mAlertDialog.show();
    }

    void dismiss() {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            return;
        }
        mAlertDialog.dismiss();
        mAlertDialog = null;
    }
}
