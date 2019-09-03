# Triple Banana Project

The "Triple Banana Project" is an effort to develop a new modern browser that
reflects the customer's needs as soon as possible. This project is based on
[Chromium](https://www.chromium.org) project and still undergoing extensive
experimentation.

## Build

1. [Checking out and building Chromium for Android](
https://chromium.googlesource.com/chromium/src/+/master/docs/android_build_instructions.md)
2. Checking out `triple_banana` repository in `//chromium/src`
   ```sh
   chromium/src$ git clone https://github.com/romandev/triple_banana
   ```
3. Update Chromium repository and apply upstream patches for triple banana
   ```sh
   chromium/src$ ./triple_banana/tools/banana update_chromium
   ```
4. Build TripleBanana.apk
   ```sh
   chromium/src$ ninja -C out/Default triple_banana
   ```
5. Then, you can check the `TripleBanana.apk` under `out/Default/apks/`

## Update upstream changes

1. Make changes in `chromium/src` but it SHOULD be committed.
2. Run `update_patches` command
   ```sh
   chromium/src/triple_banana$ ./tools/banana update_patches
   ```
3. Commit upstream changes
   ```sh
   chromium/src/triple_banana$ git add upstream/ && git commit
   ```
