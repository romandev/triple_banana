// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#include <jni.h>

#include "base/android/jni_string.h"
#include "chrome/android/chrome_jni_headers/CakeLocalState_jni.h"
#include "chrome/browser/browser_process.h"
#include "components/prefs/pref_service.h"

static base::android::ScopedJavaLocalRef<jstring> JNI_CakeLocalState_GetString(
    JNIEnv* env,
    const base::android::JavaParamRef<jstring>& key) {
  const std::string& value = g_browser_process->local_state()->GetString(
      base::android::ConvertJavaStringToUTF8(key));
  return base::android::ConvertUTF8ToJavaString(env, value);
}
