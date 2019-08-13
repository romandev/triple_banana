// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_SERVICES_HELLO_HELLO_H_
#define TRIPLE_BANANA_SERVICES_HELLO_HELLO_H_

#include "base/macros.h"
#include "triple_banana/services/hello/public/mojom/hello.mojom.h"

namespace hello {

class Hello : public mojom::Hello {
 public:
  Hello();
  ~Hello() override;

  void Ping(int number, PingCallback callback) override;

 private:
  DISALLOW_COPY_AND_ASSIGN(Hello);
};

}  // namespace hello

#endif  // TRIPLE_BANANA_SERVICES_HELLO_HELLO_H_
