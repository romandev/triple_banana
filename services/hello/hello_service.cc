// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "triple_banana/services/hello/hello_service.h"

#include <memory>
#include <string>
#include <utility>
#include "base/bind.h"
#include "services/service_manager/public/mojom/service.mojom.h"
#include "triple_banana/services/hello/hello.h"

#include "base/android/jni_android.h"
#include "triple_banana/services/hello/jni_headers/InterfaceRegistrar_jni.h"

namespace hello {

HelloService::HelloService(service_manager::mojom::ServiceRequest request)
    : service_binding_(this, std::move(request)) {
#if defined(OS_ANDROID)
  registry_.AddInterface(
      GetJavaInterfaces()->CreateInterfaceFactory<mojom::Hello>());
#endif
}

HelloService::~HelloService() = default;

void HelloService::OnConnect(const service_manager::ConnectSourceInfo& source,
                             const std::string& interface_name,
                             mojo::ScopedMessagePipeHandle pipe) {
  registry_.BindInterface(interface_name, std::move(pipe));
}

#if defined(OS_ANDROID)
service_manager::InterfaceProvider* HelloService::GetJavaInterfaces() {
  if (!java_interface_provider_) {
    service_manager::mojom::InterfaceProviderPtr provider;
    Java_InterfaceRegistrar_createInterfaceRegistryForContext(
        base::android::AttachCurrentThread(),
        mojo::MakeRequest(&provider).PassMessagePipe().release().value());
    java_interface_provider_ =
        std::make_unique<service_manager::InterfaceProvider>();
    java_interface_provider_->Bind(std::move(provider));
  }
  return java_interface_provider_.get();
}
#endif

}  // namespace hello
