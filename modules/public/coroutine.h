// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_

#include <memory>
#include "base/bind.h"

using Coroutine = std::function<void(void*, void*)>;

#define co_resume_with_data(coroutine, result_ref) \
  (*coroutine)(coroutine, result_ref)

#define co_resume(coroutine) (*coroutine)(coroutine, nullptr)

#define co_this static_cast<Coroutine*>(__self)

#define co_begin(unused)  \
  int __pending_line = 0; \
  auto* __coroutine = new Coroutine([=](void* __self, void* __data) mutable { \
    switch (__pending_line) { \
    case 0:;
#define co_yield(unused)       \
  do {                         \
    __pending_line = __LINE__; \
    return;                    \
    case __LINE__:;            \
  } while (0)

#define co_return(unused) \
  __pending_line = 0;     \
  }                       \
  delete co_this;         \
  return;                 \
  });                     \
  co_resume(__coroutine)

#define co_await_overload(_0, _1, _2, _3, _4, _5, _6, _7, _8, _9, name, ...) \
  name
#define co_await(...)                                                          \
  co_await_overload(_0, ##__VA_ARGS__, co_await_with_args, co_await_with_args, \
                    co_await_with_args, co_await_with_args,                    \
                    co_await_with_args, co_await_with_args,                    \
                    co_await_with_args, co_await_with_no_args,                 \
                    co_await_with_no_args, co_await_with_no_args)(__VA_ARGS__)

#define co_await_with_no_args(result_type, method_name)            \
  ({                                                               \
    method_name(base::BindOnce(                                    \
        [](Coroutine* c, result_type result) {                     \
          co_resume_with_data(                                     \
              c, const_cast<std::decay_t<result_type>*>(&result)); \
        },                                                         \
        co_this));                                                 \
    co_yield();                                                    \
    *static_cast<std::decay_t<result_type>*>(__data);              \
  })

#define co_await_with_args(result_type, method_name, ...)                      \
  ({                                                                           \
    method_name(__VA_ARGS__,                                                   \
                base::BindOnce(                                                \
                    [](Coroutine* c, result_type result) {                     \
                      co_resume_with_data(                                     \
                          c, const_cast<std::decay_t<result_type>*>(&result)); \
                    },                                                         \
                    co_this));                                                 \
    co_yield();                                                                \
    *static_cast<std::decay_t<result_type>*>(__data);                          \
  })

#define DEFINE_DEFAULT_OPERATORS(MoveableType)                         \
 public:                                                               \
  Copyable() = default;                                                \
  Copyable(MoveableType&& value) : value_(std::move(value)) {}         \
  Copyable(Copyable&& other) : value_(std::move(other.value_)) {}      \
  Copyable(const Copyable& other) : value_(std::move(other.value_)) {} \
  Copyable& operator=(MoveableType&& value) {                          \
    value_ = std::move(value);                                         \
    return *this;                                                      \
  }                                                                    \
  Copyable& operator=(Copyable&& other) {                              \
    value_ = std::move(other.value_);                                  \
    return *this;                                                      \
  }                                                                    \
  Copyable& operator=(const Copyable& other) {                         \
    value_ = std::move(other.value_);                                  \
    return *this;                                                      \
  }                                                                    \
  operator MoveableType &&()&& { return std::move(value_); }           \
                                                                       \
 private:                                                              \
  mutable MoveableType value_

template <typename Moveable>
class Copyable {
  DEFINE_DEFAULT_OPERATORS(Moveable);
};

template <typename InternalType>
class Copyable<std::unique_ptr<InternalType>> {
  DEFINE_DEFAULT_OPERATORS(std::unique_ptr<InternalType>);

 public:
  operator bool() { return static_cast<bool>(value_); }

  auto operator-> () const { return value_.operator->(); }

  template <typename ResetType>
  void reset(ResetType value) {
    value_.reset(value);
  }

  auto get() { return value_.get(); }
};

template <typename InternalType>
class Copyable<base::OnceCallback<InternalType>> {
  DEFINE_DEFAULT_OPERATORS(base::OnceCallback<InternalType>);

 public:
  template <typename... Args>
  auto Run(Args... args) {
    return std::move(value_).Run(std::forward<Args>(args)...);
  }
};

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_
