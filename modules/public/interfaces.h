// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_

#include "triple_banana/modules/public/connector.h"
#include "triple_banana/modules/public/coroutine.h"
#include "triple_banana/modules/public/mojom/adblock.mojom.h"
#include "triple_banana/modules/public/mojom/authentication.mojom.h"
#include "triple_banana/modules/public/mojom/encrypter.mojom.h"
#include "triple_banana/modules/public/mojom/hello.mojom.h"

#define EXPORT_INTERFACE_LIST                   \
  adblock::mojom::FilterLoader,                 \
  authentication::mojom::AuthenticationManager, \
      encrypter::mojom::EncrypterManager, hello::mojom::Hello

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_INTERFACES_H_
