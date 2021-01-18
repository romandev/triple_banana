// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.toolbar;

import android.content.Context;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.triple.banana.util.CountryCodeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            BananaApplicationUtils.get()
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
        String data = BananaApplicationUtils.get()
                              .getApplicationContext()
                              .getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
                              .getString(SAVED_DATA, null);
        ArrayList<ButtonId> arrayList = new ArrayList<>();

        try {
            if (data != null) {
                JSONArray arr = new JSONArray(data);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    ButtonId buttonId =
                            ButtonId.getButtonId(jsonObject.getString(Integer.toString(i)));

                    if (buttonId != null) arrayList.add(buttonId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<ButtonId> buttonIds = new ArrayList<>();
        for (ButtonId id : ButtonId.values()) buttonIds.add(id);

        boolean isIndiaDevice = "IN".equals(CountryCodeUtil.getCountryCode());
        ButtonId replaceId = isIndiaDevice ? ButtonId.AT_ME_GAME : ButtonId.DARK_MODE;
        Collections.swap(
                buttonIds, buttonIds.indexOf(ButtonId.SHARE), buttonIds.indexOf(replaceId));
        Collections.swap(buttonIds, buttonIds.indexOf(ButtonId.NEW_TAB),
                buttonIds.indexOf(ButtonId.TAB_SWITCHER));

        for (ButtonId id : buttonIds) {
            if (!arrayList.contains(id)) {
                arrayList.add(id);
            }
        }

        final int QUICK_MENU_INDEX = 5;
        Collections.swap(arrayList, QUICK_MENU_INDEX, arrayList.indexOf(ButtonId.BANANA_EXTENSION));

        return arrayList;
    }
}
