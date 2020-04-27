// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.triple.banana.R;

import org.chromium.chrome.browser.SnackbarActivity;

/**
 * The activity for editing the browsing mode bottom toolbar.
 */
public class ToolbarEditActivity extends SnackbarActivity {
    private ToolbarEditController mToolbarEditController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_edit_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mToolbarEditController =
                new ToolbarEditController(findViewById(R.id.selected_recycler_view),
                        findViewById(R.id.candidate_recycler_view));
        mToolbarEditController.setButtonList(
                ToolbarStateModel.getInstance().getToolbarButtonList());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_editor);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_action_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close_menu_id) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.done_menu_id) {
            mToolbarEditController.commit();
            finish();
            return true;
        } else if (item.getItemId() == R.id.refresh_menu_id) {
            mToolbarEditController.initialization();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mToolbarEditController.isToolbarChanged()) {
            showSavePopup();
        } else {
            super.onBackPressed();
        }
    }

    private void showSavePopup() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_suggestion_message)
                .setPositiveButton(R.string.save,
                        (dialog, id) -> {
                            mToolbarEditController.commit();
                            finish();
                        })
                .setNegativeButton(R.string.discard, (dialog, id) -> { finish(); })
                .setNeutralButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }
}
