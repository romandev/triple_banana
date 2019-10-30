// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_
#define TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_

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

template <typename T>
class CopyableWrapper {
 public:
  CopyableWrapper(T&& t) : value(std::move(t)) {}
  CopyableWrapper(CopyableWrapper const& other)
      : value(std::move(other.value)) {}
  CopyableWrapper(CopyableWrapper&& other) : value(std::move(other.value)) {}
  CopyableWrapper& operator=(CopyableWrapper const& other) {
    value = std::move(other.value);
    return *this;
  }

  CopyableWrapper& operator=(CopyableWrapper&& other) {
    value = std::move(other.value);
    return *this;
  }

  operator T &&() && { return std::move(value); }

 private:
  mutable T value;
};

template <typename T>
CopyableWrapper<T> make_copyable(T&& value) {
  return CopyableWrapper<T>(std::move(value));
}

#endif  // TRIPLE_BANANA_MODULES_PUBLIC_COROUTINE_H_
