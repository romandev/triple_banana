// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_SERVICES_HELLO_HELLO_SERVICE_H_
#define TRIPLE_BANANA_SERVICES_HELLO_HELLO_SERVICE_H_

#include <string>
#include "base/macros.h"
#include "mojo/public/cpp/bindings/unique_receiver_set.h"
#include "services/service_manager/public/cpp/binder_map.h"
#include "services/service_manager/public/cpp/service.h"
#include "services/service_manager/public/cpp/service_binding.h"
#include "services/service_manager/public/mojom/service.mojom.h"
#include "triple_banana/services/hello/public/mojom/hello.mojom.h"

namespace hello {

class HelloService : public service_manager::Service {
 public:
  explicit HelloService(service_manager::mojom::ServiceRequest request);
  ~HelloService() override;

 private:
  void OnConnect(const service_manager::ConnectSourceInfo& source,
                 const std::string& interface_name,
                 mojo::ScopedMessagePipeHandle pipe) override;

  void BindHello(mojo::PendingReceiver<mojom::Hello> receiver);

  mojo::UniqueReceiverSet<mojom::Hello> hello_receivers_;

  service_manager::ServiceBinding service_binding_;
  service_manager::BinderMap binders_;

  DISALLOW_COPY_AND_ASSIGN(HelloService);
};

}  // namespace hello

#endif  // TRIPLE_BANANA_SERVICES_HELLO_HELLO_SERVICE_H_
