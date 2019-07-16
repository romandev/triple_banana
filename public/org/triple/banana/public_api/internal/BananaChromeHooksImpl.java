// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.internal;

import org.triple.banana.hooks_api.ChromeHooks;
import org.triple.banana.hooks_api.ChromeHooksAPI;
import org.triple.banana.public_api.export.BananaCommandLine;
import org.triple.banana.public_api.export.BananaHooks;

public class BananaChromeHooksImpl implements ChromeHooksAPI {
    private BananaHooks mImpl;

    public BananaChromeHooksImpl(BananaHooks impl) {
        ChromeHooks.api.setImpl(this);
        mImpl = impl;
    }

    private BananaHooks getImpl() {
        assert mImpl != null;
        return mImpl;
    }

    @Override
    public void initCommandLine() {
        getImpl().initCommandLine(BananaCommandLine.instance);
    }
}
