// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

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
