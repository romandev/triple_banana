// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.modules;

import org.chromium.mojo.bindings.InterfaceRequest;
import org.chromium.mojo.bindings.Interface;
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
