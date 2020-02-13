package org.triple.banana.toolbar;

import android.content.Context;
import android.content.Intent;

public class ToolbarEditor {
    public static void show(Context context) {
        Intent intent = new Intent(context, ToolbarEditActivity.class);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
