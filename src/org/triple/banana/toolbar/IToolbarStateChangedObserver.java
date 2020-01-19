// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import java.util.ArrayList;

public interface IToolbarStateChangedObserver {
    void onToolbarStateChanged(ArrayList<ButtonId> hashMap);
}
