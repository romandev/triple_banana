// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.content.Context;

import org.banana.cake.interfaces.BananaContextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ToolbarStatePreferenceStoreImpl implements IToolbarStatePersistentStore {
    static final String PREFERENCE_FILE_NAME = "toolbar_state";
    static final String SAVED_DATA = "savedData";

    @Override
    public void storeStateToPersistentStore(ArrayList<ButtonId> arrayList) {
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Integer.toString(i), arrayList.get(i));
                array.put(jsonObject);
            }
            BananaContextUtils.get()
                    .getApplicationContext()
                    .getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(SAVED_DATA, array.toString())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ButtonId> loadStateFromPersistentStore() {
        String data = BananaContextUtils.get()
                              .getApplicationContext()
                              .getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
                              .getString(SAVED_DATA, null);
        ArrayList<ButtonId> arrayList = new ArrayList<>();
        try {
            if (data != null) {
                JSONArray arr = new JSONArray(data);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    arrayList.add(ButtonId.valueOf(jsonObject.getString(Integer.toString(i))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (arrayList.isEmpty()) {
            for (ButtonId id : ButtonId.values()) {
                arrayList.add(id);
            }
        }

        return arrayList;
    }
}
