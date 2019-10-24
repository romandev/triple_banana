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

#define co_begin(unused) \
  auto* __coroutine = new Coroutine([=](void* __self, void* __data) mutable { \
    static int __pending_line = 0; \
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
#define co_await(...)                                                      \
  co_await_overload(_0, ##__VA_ARGS__, co_await9, co_await8, co_await7,    \
                    co_await6, co_await5, co_await4, co_await3, co_await2, \
                    co_await1, co_await0)(__VA_ARGS__)

// FIXME: We should reduce duplicated code
#define co_await2(method, result_type)                       \
  ({                                                         \
    method(base::BindOnce(                                   \
        [](Coroutine* c, result_type result) {               \
          result_type* result_ref = new result_type();       \
          *result_ref = result;                              \
          co_resume_with_data(c, result_ref);                \
        },                                                   \
        co_this));                                           \
    co_yield();                                              \
    result_type result = *static_cast<result_type*>(__data); \
    delete static_cast<result_type*>(__data);                \
    result;                                                  \
  })
#define co_await3(method, result_type, arg1)                        \
  ({                                                                \
    method(arg1, base::BindOnce(                                    \
                     [](Coroutine* c, result_type result) {         \
                       result_type* result_ref = new result_type(); \
                       *result_ref = result;                        \
                       co_resume_with_data(c, result_ref);          \
                     },                                             \
                     co_this));                                     \
    co_yield();                                                     \
    result_type result = *static_cast<result_type*>(__data);        \
    delete static_cast<result_type*>(__data);                       \
    result;                                                         \
  })
#define co_await4(method, result_type, arg1, arg2)            \
  ({                                                          \
    method(arg1, arg2,                                        \
           base::BindOnce(                                    \
               [](Coroutine* c, result_type result) {         \
                 result_type* result_ref = new result_type(); \
                 *result_ref = result;                        \
                 co_resume_with_data(c, result_ref);          \
               },                                             \
               co_this));                                     \
    co_yield();                                               \
    result_type result = *static_cast<result_type*>(__data);  \
    delete static_cast<result_type*>(__data);                 \
    result;                                                   \
  })
#define co_await5(method, result_type, arg1, arg2, arg3)      \
  ({                                                          \
    method(arg1, arg2,                                        \
           arg3 base::BindOnce(                               \
               [](Coroutine* c, result_type result) {         \
                 result_type* result_ref = new result_type(); \
                 *result_ref = result;                        \
                 co_resume_with_data(c, result_ref);          \
               },                                             \
               co_this));                                     \
    co_yield();                                               \
    result_type result = *static_cast<result_type*>(__data);  \
    delete static_cast<result_type*>(__data);                 \
    result;                                                   \
  })
#define co_await6(method, result_type, arg1, arg2, arg3, arg4) \
  ({                                                           \
    method(arg1, arg2, arg3,                                   \
           arg4 base::BindOnce(                                \
               [](Coroutine* c, result_type result) {          \
                 result_type* result_ref = new result_type();  \
                 *result_ref = result;                         \
                 co_resume_with_data(c, result_ref);           \
               },                                              \
               co_this));                                      \
    co_yield();                                                \
    result_type result = *static_cast<result_type*>(__data);   \
    delete static_cast<result_type*>(__data);                  \
    result;                                                    \
  })
#define co_await7(method, result_type, arg1, arg2, arg3, arg4, arg5) \
  ({                                                                 \
    method(arg1, arg2, arg3, arg4,                                   \
           arg5 base::BindOnce(                                      \
               [](Coroutine* c, result_type result) {                \
                 result_type* result_ref = new result_type();        \
                 *result_ref = result;                               \
                 co_resume_with_data(c, result_ref);                 \
               },                                                    \
               co_this));                                            \
    co_yield();                                                      \
    result_type result = *static_cast<result_type*>(__data);         \
    delete static_cast<result_type*>(__data);                        \
    result;                                                          \
  })
#define co_await8(method, result_type, arg1, arg2, arg3, arg4, arg5, arg6) \
  ({                                                                       \
    method(arg1, arg2, arg3, arg4, arg5,                                   \
           arg6 base::BindOnce(                                            \
               [](Coroutine* c, result_type result) {                      \
                 result_type* result_ref = new result_type();              \
                 *result_ref = result;                                     \
                 co_resume_with_data(c, result_ref);                       \
               },                                                          \
               co_this));                                                  \
    co_yield();                                                            \
    result_type result = *static_cast<result_type*>(__data);               \
    delete static_cast<result_type*>(__data);                              \
    result;                                                                \
  })
#define co_await9(method, result_type, arg1, arg2, arg3, arg4, arg5, arg6, \
                  arg7)                                                    \
  ({                                                                       \
    method(arg1, arg2, arg3, arg4, arg5, arg6,                             \
           arg7 base::BindOnce(                                            \
               [](Coroutine* c, result_type result) {                      \
                 result_type* result_ref = new result_type();              \
                 *result_ref = result;                                     \
                 co_resume_with_data(c, result_ref);                       \
               },                                                          \
               co_this));                                                  \
    co_yield();                                                            \
    result_type result = *static_cast<result_type*>(__data);               \
    delete static_cast<result_type*>(__data);                              \
    result;                                                                \
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
