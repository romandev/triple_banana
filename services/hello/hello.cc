// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "triple_banana/services/hello/hello.h"

#include <utility>

namespace hello {

Hello::Hello() = default;

Hello::~Hello() = default;

void Hello::Ping(int number, PingCallback callback) {
  std::move(callback).Run(number);
}

}  // namespace hello
