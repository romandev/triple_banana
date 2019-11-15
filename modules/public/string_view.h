// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_STRING_VIEW_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_STRING_VIEW_H_

class string_view {
 private:
  const char* data_;
  size_t length_;

 public:
  template <size_t N>
  constexpr string_view(const char (&data)[N]) : data_(data), length_(N - 1) {}
  constexpr string_view(const char* data, size_t length)
      : data_(data), length_(length) {}

  constexpr const char* data() const { return data_; }

  constexpr size_t length() const { return length_; }

  constexpr string_view SubString(size_t begin_offset, size_t length) const {
    return {data_ + begin_offset, length};
  }

  constexpr size_t find(const string_view& other) const {
    // TODO(zino): We should implement string searching algorithm.
    return 0;
  }

  constexpr bool operator==(const string_view& other) const {
    if (length_ != other.length_)
      return false;
    for (size_t i = 0; i < length(); i++) {
      if (data_[i] != other.data_[i])
        return false;
    }
    return true;
  }
};

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_STRING_VIEW_H_
