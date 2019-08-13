// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_SERVICES_HELLO_PUBLIC_CPP_MANIFEST_H_
#define TRIPLE_BANANA_SERVICES_HELLO_PUBLIC_CPP_MANIFEST_H_

namespace service_manager {
class Manifest;
}  // namespace service_manager

namespace hello {

const service_manager::Manifest& GetManifest();

}  // namespace hello

#endif  // TRIPLE_BANANA_SERVICES_HELLO_PUBLIC_CPP_MANIFEST_H_
