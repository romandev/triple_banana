// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "triple_banana/modules/public/manifest/manifest.h"

#include "base/no_destructor.h"
#include "services/service_manager/public/cpp/manifest.h"
#include "services/service_manager/public/cpp/manifest_builder.h"
#include "triple_banana/modules/public/mojom/authentication.mojom.h"
#include "triple_banana/modules/public/mojom/encrypter.mojom.h"
#include "triple_banana/modules/public/mojom/hello.mojom.h"

namespace triple_banana {

const service_manager::Manifest& GetManifest() {
  static base::NoDestructor<service_manager::Manifest> manifest{
      service_manager::ManifestBuilder()
          .WithServiceName("triple_banana")
          .WithDisplayName("triple_banana")
          .WithOptions(
              service_manager::ManifestOptionsBuilder()
                  .WithExecutionMode(service_manager::Manifest::ExecutionMode::
                                         kInProcessBuiltin)
                  .Build())
          .ExposeCapability(
              "modules",
              service_manager::Manifest::InterfaceList<
                  authentication::mojom::AuthenticationManager,
                  encrypter::mojom::EncrypterManager, hello::mojom::Hello>())
          .Build()};
  return *manifest;
}

}  // namespace triple_banana
