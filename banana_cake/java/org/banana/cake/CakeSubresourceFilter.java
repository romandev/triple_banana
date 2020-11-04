// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaSubresourceFilter;

import org.chromium.base.annotations.NativeMethods;

public class CakeSubresourceFilter implements BananaSubresourceFilter {
    @Override
    public void install(@NonNull String rulesetPath, @NonNull Runnable successCallback) {
        CakeSubresourceFilterJni.get().install(rulesetPath, successCallback);
    }

    @Override
    public void reset() {
        CakeSubresourceFilterJni.get().reset();
    }

    @Override
    public @NonNull String getVersion() {
        return CakeSubresourceFilterJni.get().getVersion();
    }

    @NativeMethods
    interface Natives {
        void install(@NonNull String rulesetPath, @NonNull Runnable successCallback);
        void reset();
        @NonNull String getVersion();
    }
}
