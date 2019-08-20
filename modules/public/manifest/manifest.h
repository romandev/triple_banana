// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_MANIFEST_MANIFEST_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_MANIFEST_MANIFEST_H_

namespace service_manager {
class Manifest;
}  // namespace service_manager

namespace triple_banana {

const service_manager::Manifest& GetManifest();

}  // namespace triple_banana

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_MANIFEST_MANIFEST_H_
