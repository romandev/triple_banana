// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.hello;

import org.triple.banana.hello.mojom.Hello;

import org.chromium.mojo.system.MojoException;
import org.chromium.services.service_manager.InterfaceFactory;

public class HelloImpl implements Hello {
    @Override
    public void ping(int number, PingResponse callback) {
        callback.call(number);
    }

    @Override
    public void close() {}

    @Override
    public void onConnectionError(MojoException e) {}

    public static class Factory implements InterfaceFactory<Hello> {
        public Factory() {}

        @Override
        public Hello createImpl() {
            return new HelloImpl();
        }
    }
}
