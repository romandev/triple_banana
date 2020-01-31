// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.banana.cake.bootstrap.BananaApplication;
import org.banana.cake.interfaces.BananaTabManager;

public class TripleBananaApplication extends BananaApplication {
    static {
        InterfaceProvider.initialize();
        BananaTabManager.get().addObserver(
                bananaTab -> { MediaSuspendController.instance.DisableOnYouTube(bananaTab); });
    }
}
