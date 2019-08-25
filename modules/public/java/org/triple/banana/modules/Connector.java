// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.modules;

import org.chromium.mojo.bindings.InterfaceRequest;
import org.chromium.mojo.bindings.Interface;
import org.chromium.mojo.system.MessagePipeHandle;
import org.chromium.mojo.system.Pair;
import org.chromium.mojo.system.impl.CoreImpl;
import org.chromium.content.common.ServiceManagerConnectionImpl;

public class Connector {
    private static org.chromium.services.service_manager.Connector sConnector;

    private static org.chromium.services.service_manager.Connector getSystemConnector() {
        if (sConnector == null) {
            sConnector = new org.chromium.services.service_manager.Connector(ServiceManagerConnectionImpl.getConnectorMessagePipeHandle());
        }
        return sConnector;
    }

    public static <ReturnType extends Interface, Manager extends Interface.Manager> ReturnType bindInterface(Manager manager) {
        Pair<ReturnType, InterfaceRequest<ReturnType>> pair = manager.getInterfaceRequest(CoreImpl.getInstance());
        getSystemConnector().bindInterface("triple_banana", manager.getName(), pair.second);
        return pair.first;
    }
}
