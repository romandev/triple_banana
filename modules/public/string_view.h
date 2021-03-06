// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
    for (size_t i = 0; i < length() - other.length(); i++) {
      bool found = true;
      for (size_t j = 0; j < other.length(); j++) {
        if (data_[i + j] != other.data_[j]) {
          found = false;
          break;
        }
      }
      if (found)
        return i;
    }
    return std::string::npos;
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
