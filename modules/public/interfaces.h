// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_

#include <string>
#include "content/public/child/child_thread.h"
#include "mojo/public/cpp/bindings/remote.h"
#include "third_party/blink/public/common/thread_safe_browser_interface_broker_proxy.h"
#include "third_party/blink/public/platform/platform.h"
#include "triple_banana/modules/public/coroutine.h"
#include "triple_banana/modules/public/module_service.h"
#include "triple_banana/modules/public/mojom/adblock.mojom.h"
#include "triple_banana/modules/public/mojom/authentication.mojom.h"
#include "triple_banana/modules/public/mojom/download.mojom.h"
#include "triple_banana/modules/public/mojom/encrypter.mojom.h"
#include "triple_banana/modules/public/mojom/hello.mojom.h"
#include "triple_banana/modules/public/mojom/media.mojom-blink.h"
#include "triple_banana/modules/public/string_view.h"

#define AutoBind(interface_name)               \
  triple_banana::BindInterface<interface_name, \
                               triple_banana::GetBinderType(__FILE__)>()()

#define BIND_HOST_RECEIVER(interface_name)                                \
  if (auto r = receiver.As<interface_name>()) {                           \
    ModuleService::Get().GetJavaInterfaces()->GetInterface(std::move(r)); \
    return;                                                               \
  }

namespace triple_banana {

enum class BinderType { BROWSER, RENDERER, BLINK };

inline constexpr BinderType GetBinderType(const string_view& file_name) {
  if (file_name.find("/browser/") != std::string::npos)
    return BinderType::BROWSER;
  else if (file_name.find("/third_party/blink/") != std::string::npos) {
    return BinderType::BLINK;
  }
  return BinderType::RENDERER;
}

template <typename Interface, BinderType binder>
struct BindInterface {
  mojo::Remote<Interface> operator()() {
    mojo::Remote<Interface> remote_interface;
    return remote_interface;
  }
};

template <typename Interface>
struct BindInterface<Interface, BinderType::BROWSER> {
  mojo::Remote<Interface> operator()() {
    mojo::Remote<Interface> remote_interface;
    ModuleService::Get().GetJavaInterfaces()->GetInterface(
        remote_interface.BindNewPipeAndPassReceiver());
    return remote_interface;
  }
};

template <typename Interface>
struct BindInterface<Interface, BinderType::BLINK> {
  mojo::Remote<Interface> operator()() {
    mojo::Remote<Interface> remote_interface;
    blink::Platform::Current()->GetBrowserInterfaceBroker()->GetInterface(
        remote_interface.BindNewPipeAndPassReceiver());
    return remote_interface;
  }
};

template <typename Interface>
struct BindInterface<Interface, BinderType::RENDERER> {
  mojo::Remote<Interface> operator()() {
    mojo::Remote<Interface> remote_interface;
    content::ChildThread::Get()->BindHostReceiver(
        remote_interface.BindNewPipeAndPassReceiver());
    return remote_interface;
  }
};

inline void OnBindHostReceiverForRenderer(
    mojo::GenericPendingReceiver receiver) {
  BIND_HOST_RECEIVER(adblock::mojom::FilterLoader);
  BIND_HOST_RECEIVER(authentication::mojom::AuthenticationManager);
  BIND_HOST_RECEIVER(encrypter::mojom::EncrypterManager);
  BIND_HOST_RECEIVER(hello::mojom::Hello);
  BIND_HOST_RECEIVER(media::mojom::blink::BananaMediaEventDispatcher);
}

}  // namespace triple_banana

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_
