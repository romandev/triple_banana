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

namespace hello {

HelloService::HelloService(service_manager::mojom::ServiceRequest request)
    : service_binding_(this, std::move(request)) {
  binders_.Add(
      base::BindRepeating(&HelloService::BindHello, base::Unretained(this)));
}

HelloService::~HelloService() = default;

void HelloService::OnConnect(const service_manager::ConnectSourceInfo& source,
                             const std::string& interface_name,
                             mojo::ScopedMessagePipeHandle pipe) {
  binders_.TryBind(interface_name, &pipe);
}

void HelloService::BindHello(mojo::PendingReceiver<mojom::Hello> receiver) {
  hello_receivers_.Add(std::make_unique<Hello>(), std::move(receiver));
}

}  // namespace hello
