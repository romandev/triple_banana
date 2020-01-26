// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_

#include <memory>
#include "base/macros.h"
#include "services/service_manager/public/cpp/interface_provider.h"

namespace service_manager {
class InterfaceProvider;
}  // namespace service_manager

namespace triple_banana {

class ModuleService {
 public:
  explicit ModuleService();
  virtual ~ModuleService();

  static ModuleService& Get();

  // Binds |java_interface_provider_| to an interface registry that exposes
  // factories for the interfaces that are provided via Java on Android.
  service_manager::InterfaceProvider* GetJavaInterfaces();

 private:
#if defined(OS_ANDROID)
  // InterfaceProvider that is bound to the Java-side interface registry.
  std::unique_ptr<service_manager::InterfaceProvider> java_interface_provider_;
#endif
  DISALLOW_COPY_AND_ASSIGN(ModuleService);
};

}  // namespace triple_banana

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_MODULE_SERVICE_H_
