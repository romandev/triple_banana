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
3. Apply patches in Chromium upstream for triple banana
   ```sh
   chromium/src$ git am triple_banana/upstream/*.patch
   ```
4. Build TripleBanana.apk
   ```sh
   chromium/src$ ninja -C out/Default triple_banana
   ```
5. Then, you can check the `TripleBanana.apk` under `out/Default/apks/`
