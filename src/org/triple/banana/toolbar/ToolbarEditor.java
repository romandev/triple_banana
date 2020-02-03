package org.triple.banana.toolbar;

import android.content.Context;
import android.content.Intent;

import org.banana.cake.interfaces.BananaToolbarEditor;

public class ToolbarEditor implements BananaToolbarEditor {
    @Override
    public void show(Context context) {
        Intent intent = new Intent(context, ToolbarEditActivity.class);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
