// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#include <jni.h>
#include "base/android/jni_string.h"
#include "base/android/task_scheduler/post_task_android.h"
#include "base/logging.h"
#include "chrome/android/chrome_jni_headers/CakeSubresourceFilter_jni.h"
#include "chrome/browser/browser_process.h"
#include "components/subresource_filter/content/browser/ruleset_service.h"
#include "components/subresource_filter/content/browser/ruleset_version.h"

static void JNI_CakeSubresourceFilter_Install(
    JNIEnv* env,
    const base::android::JavaParamRef<jstring>& ruleset_path_string,
    const base::android::JavaParamRef<jobject>& success_callback,
    const base::android::JavaParamRef<jstring>& runnable_class_name) {
  base::FilePath ruleset_path(
      ConvertJavaStringToUTF8(env, ruleset_path_string));
  std::string new_version = ruleset_path.BaseName().RemoveExtension().value();

  subresource_filter::UnindexedRulesetInfo ruleset_info;
  ruleset_info.ruleset_path = ruleset_path;
  ruleset_info.content_version = new_version;

  auto* ruleset_service =
      g_browser_process->subresource_filter_ruleset_service();
  ruleset_service->SetRulesetPublishedCallbackForTesting(base::BindOnce(
      &base::PostTaskAndroid::RunJavaTask,
      base::android::ScopedJavaGlobalRef<jobject>(success_callback),
      ConvertJavaStringToUTF8(env, runnable_class_name)));
  ruleset_service->IndexAndStoreAndPublishRulesetIfNeeded(ruleset_info);
}

static void JNI_CakeSubresourceFilter_Reset(JNIEnv* env) {
  subresource_filter::IndexedRulesetVersion empty_version;
  empty_version.SaveToPrefs(g_browser_process->local_state());
}

static base::android::ScopedJavaLocalRef<jstring>
JNI_CakeSubresourceFilter_GetVersion(JNIEnv* env) {
  auto* ruleset_service =
      g_browser_process->subresource_filter_ruleset_service();
  return base::android::ConvertUTF8ToJavaString(
      env, ruleset_service->GetMostRecentlyIndexedVersion().content_version);
}
