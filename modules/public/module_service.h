// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_

#include <memory>
#include <string>
#include "base/macros.h"
#include "mojo/public/cpp/system/message_pipe.h"
#include "services/service_manager/public/cpp/bind_source_info.h"
#include "services/service_manager/public/cpp/binder_registry.h"
#include "services/service_manager/public/cpp/interface_provider.h"
#include "services/service_manager/public/cpp/service.h"
#include "services/service_manager/public/cpp/service_binding.h"
#include "services/service_manager/public/mojom/service.mojom.h"

namespace service_manager {
class InterfaceProvider;
}  // namespace service_manager

namespace triple_banana {

class ModuleService : public service_manager::Service {
 public:
  explicit ModuleService(service_manager::mojom::ServiceRequest request);
  ~ModuleService() override;

 private:
  void OnConnect(const service_manager::ConnectSourceInfo& source,
                 const std::string& interface_name,
                 mojo::ScopedMessagePipeHandle pipe) override;

#if defined(OS_ANDROID)
  // Binds |java_interface_provider_| to an interface registry that exposes
  // factories for the interfaces that are provided via Java on Android.
  service_manager::InterfaceProvider* GetJavaInterfaces();

  template <typename InterfaceType>
  int AddJavaInterfaceInternal() {
    registry_.AddInterface(
        GetJavaInterfaces()->CreateInterfaceFactory<InterfaceType>());
    return 0;
  }

  template <typename... InterfaceTypes>
  void AddJavaInterfaces() {
    [](...) {}(AddJavaInterfaceInternal<InterfaceTypes>()...);
  }

  // InterfaceProvider that is bound to the Java-side interface registry.
  std::unique_ptr<service_manager::InterfaceProvider> java_interface_provider_;
#endif

  service_manager::ServiceBinding service_binding_;
  service_manager::BinderRegistry registry_;

  DISALLOW_COPY_AND_ASSIGN(ModuleService);
};

}  // namespace triple_banana

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_
