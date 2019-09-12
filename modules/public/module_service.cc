// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "triple_banana/modules/public/module_service.h"

#include <utility>
#include "base/android/jni_android.h"
#include "services/service_manager/public/cpp/interface_provider.h"
#include "triple_banana/modules/jni_headers/InterfaceRegistrar_jni.h"
#include "triple_banana/modules/public/mojom/authentication.mojom.h"
#include "triple_banana/modules/public/mojom/encrypter.mojom.h"
#include "triple_banana/modules/public/mojom/hello.mojom.h"

namespace triple_banana {

ModuleService::ModuleService(service_manager::mojom::ServiceRequest request)
    : service_binding_(this, std::move(request)) {
#if defined(OS_ANDROID)
  registry_.AddInterface(
      GetJavaInterfaces()
          ->CreateInterfaceFactory<
              authentication::mojom::AuthenticationManager>());
  registry_.AddInterface(
      GetJavaInterfaces()
          ->CreateInterfaceFactory<encrypter::mojom::EncrypterManager>());
  registry_.AddInterface(
      GetJavaInterfaces()->CreateInterfaceFactory<hello::mojom::Hello>());
#endif
}

ModuleService::~ModuleService() = default;

void ModuleService::OnConnect(const service_manager::ConnectSourceInfo& source,
                              const std::string& interface_name,
                              mojo::ScopedMessagePipeHandle pipe) {
  registry_.BindInterface(interface_name, std::move(pipe));
}

#if defined(OS_ANDROID)
service_manager::InterfaceProvider* ModuleService::GetJavaInterfaces() {
  if (!java_interface_provider_) {
    service_manager::mojom::InterfaceProviderPtr provider;
    modules::Java_InterfaceRegistrar_createInterfaceRegistryForContext(
        base::android::AttachCurrentThread(),
        mojo::MakeRequest(&provider).PassMessagePipe().release().value());
    java_interface_provider_ =
        std::make_unique<service_manager::InterfaceProvider>();
    java_interface_provider_->Bind(std::move(provider));
  }
  return java_interface_provider_.get();
}
#endif

}  // namespace triple_banana
