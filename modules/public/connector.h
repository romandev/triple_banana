// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_CONNECTOR_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_CONNECTOR_H_

#include <string>
#include "content/public/child/child_thread.h"
#include "mojo/public/cpp/bindings/remote.h"
//#include "services/service_manager/public/cpp/interface_provider.h"
#include "triple_banana/modules/public/module_service.h"
#include "triple_banana/modules/public/string_view.h"

namespace triple_banana {

inline constexpr bool IsBrowserProcess(const string_view& file_name) {
  return file_name.find("/browser/") != std::string::npos;
}

#define AutoBind(interface_name)                                \
  triple_banana::IsBrowserProcess(__FILE__)                     \
      ? triple_banana::BindInterfaceOnBrowser<interface_name>() \
      : triple_banana::BindInterfaceOnRenderer<interface_name>()

template <typename Interface>
inline mojo::Remote<Interface> BindInterfaceOnRenderer() {
  mojo::Remote<Interface> remote_interface;
  content::ChildThread::Get()->BindHostReceiver(
      remote_interface.BindNewPipeAndPassReceiver());
  return remote_interface;
}

template <typename Interface>
inline mojo::Remote<Interface> BindInterfaceOnBrowser() {
  mojo::Remote<Interface> remote_interface;
  ModuleService::Get().GetJavaInterfaces()->GetInterface(
      remote_interface.BindNewPipeAndPassReceiver());
  return remote_interface;
}

inline void OnBindHostReceiverForRenderer(
    mojo::GenericPendingReceiver receiver) {
  ModuleService::Get().GetJavaInterfaces()->GetInterfaceByName(
      *receiver.interface_name(), receiver.PassPipe());
}

}  // namespace triple_banana

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_CONNECTOR_H_
