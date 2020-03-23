// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#include "triple_banana/modules/public/module_service.h"

#include <utility>
#include "base/android/jni_android.h"
#include "base/no_destructor.h"
#include "triple_banana/modules/jni_headers/InterfaceRegistrar_jni.h"

namespace triple_banana {

ModuleService::ModuleService() = default;

ModuleService::~ModuleService() = default;

// static
ModuleService& ModuleService::Get() {
  static base::NoDestructor<ModuleService> instance;
  return *instance;
}

#if defined(OS_ANDROID)
service_manager::InterfaceProvider* ModuleService::GetJavaInterfaces() {
  if (!java_interface_provider_) {
    mojo::PendingRemote<service_manager::mojom::InterfaceProvider> provider;
    modules::Java_InterfaceRegistrar_createInterfaceRegistryForContext(
        base::android::AttachCurrentThread(),
        provider.InitWithNewPipeAndPassReceiver().PassPipe().release().value());
    java_interface_provider_ =
        std::make_unique<service_manager::InterfaceProvider>();
    java_interface_provider_->Bind(std::move(provider));
  }
  return java_interface_provider_.get();
}
#endif

}  // namespace triple_banana
