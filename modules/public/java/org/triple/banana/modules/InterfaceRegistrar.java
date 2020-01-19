// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.modules;

import org.triple.banana.adblock.FilterLoaderImpl;
import org.triple.banana.adblock.mojom.FilterLoader;
import org.triple.banana.authentication.AuthenticationManagerImpl;
import org.triple.banana.authentication.mojom.AuthenticationManager;
import org.triple.banana.encrypter.EncrypterManagerImpl;
import org.triple.banana.encrypter.mojom.EncrypterManager;
import org.triple.banana.hello.HelloImpl;
import org.triple.banana.hello.mojom.Hello;

import org.chromium.base.annotations.CalledByNative;
import org.chromium.base.annotations.JNINamespace;
import org.chromium.mojo.system.impl.CoreImpl;
import org.chromium.services.service_manager.InterfaceRegistry;

@JNINamespace("modules")
class InterfaceRegistrar {
    @CalledByNative
    static void createInterfaceRegistryForContext(int nativeHandle) {
        // Note: The bindings code manages the lifetime of this object, so it
        // is not necessary to hold on to a reference to it explicitly.
        InterfaceRegistry registry = InterfaceRegistry.create(
                CoreImpl.getInstance().acquireNativeHandle(nativeHandle).toMessagePipeHandle());
        registry.addInterface(
                FilterLoader.MANAGER, new FilterLoaderImpl.Factory());
        registry.addInterface(
                AuthenticationManager.MANAGER, new AuthenticationManagerImpl.Factory());
        registry.addInterface(EncrypterManager.MANAGER, new EncrypterManagerImpl.Factory());
        registry.addInterface(Hello.MANAGER, new HelloImpl.Factory());
    }
}
