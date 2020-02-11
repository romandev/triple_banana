# Triple Banana Project

The "Triple Banana Project" is an effort to develop a new modern browser that
reflects the customer's needs as soon as possible. This project is based on
[Chromium](https://www.chromium.org) project and still undergoing extensive
experimentation.

## Checkout source code
1. [Checking out and building Chromium for Android](
https://chromium.googlesource.com/chromium/src/+/master/docs/android_build_instructions.md)

2. Checking out `triple_banana` repository in `//chromium/src`
   ```sh
   chromium/src$ git clone https://github.com/romandev/triple_banana
   ```

## Setup banana tools
- Add `$your_chromium_path/src/triple_banana/banana/tools` to the end of your
  PATH (you will probably want to put this in your ~/.bashrc)
  ```sh
  export PATH="$PATH:/path/to/.../tools"
  ```

## Sync upstream repo with `triple_banana/BASE` version
- Update Chromium repository (including `apply_patches` actually)
  ```sh
  $ banana update_chromium
  ```
- Apply upstream changes from `triple_banana/upstream`
  ```sh
  $ banana apply_patches

## Build
- Debug build
  ```sh
  $ banana build
  ```
- Release build
  ```sh
  $ banana build --release
  ```
- Use goma (before using this command, you should set `GOMA_PATH` env variable)
  ```sh
  $ export GOMA_PATH="/path/to/goma"
  $ banana build --goma
  ```
- You can check the `TripleBanana.apk` under `out/${build_target}/apks/`

## Update upstream changes

1. Make changes in `chromium/src` but it SHOULD be committed.
2. Run `update_upstream` command
   ```sh
   chromium/src/triple_banana$ ./tools/banana update_upstream
   ```
3. Commit upstream changes
   ```sh
   chromium/src/triple_banana$ git add upstream/ && git commit
   ```
