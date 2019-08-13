// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "triple_banana/services/hello/public/cpp/manifest.h"

#include "base/no_destructor.h"
#include "services/service_manager/public/cpp/manifest.h"
#include "services/service_manager/public/cpp/manifest_builder.h"
#include "triple_banana/services/hello/public/mojom/constants.mojom.h"
#include "triple_banana/services/hello/public/mojom/hello.mojom.h"

namespace hello {

const service_manager::Manifest& GetManifest() {
  static base::NoDestructor<service_manager::Manifest> manifest{
      service_manager::ManifestBuilder()
          .WithServiceName(mojom::kServiceName)
          .WithDisplayName(mojom::kServiceName)
          .WithOptions(
              service_manager::ManifestOptionsBuilder()
                  .WithExecutionMode(service_manager::Manifest::ExecutionMode::
                                         kInProcessBuiltin)
                  .Build())
          .ExposeCapability(
              mojom::kServiceCapability,
              service_manager::Manifest::InterfaceList<mojom::Hello>())
          .Build()};
  return *manifest;
}

}  // namespace hello
