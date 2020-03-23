# 1.03 @ 80.0.3987.149

## Chromium base version
- **80.0.3987.149**

## Change log
- Update LICENSE for this project (#355)
- Implement metadata check for subresource filter (#354)
- Make RemoteConfig reusable (#353)
- Disable seed based field trial testing (#351)
- Make the filter loader download from the remote server (#350)
- Improve SimpleDownloader class (#349)
- Implement SimpleDownloader class (#345)
- Rebase 80.0.3987.149 (#344)
- Refactor subresource filtering related code in upstream patches (#342)
- Rename SafeLogin with SecureLogin (#330)
- Fix a bug that the safe login infobar switch is always enabled (#329)


# 1.02 @ 80.0.3987.132

## Chromium base version
- **80.0.3987.132**

## Minor changes
- Fix a bug toolbar button color according to theme (#320)
- Use system linker instead of crazy linker since N+ OS (#327)


# 1.00 @ 80.0.3987.132

## Chromium base version
- **80.0.3987.132**

## Major changes
- **Introduce `banana_cake` layer**
  - Provides abstraction layer to wire between `Chromium` and `triple_banana`
    via servicification, dependency injection, and abstraction
- **Ad blocker**
  - Block ads on sites that show intrusive or misleading ads
- **Safe Login**
  - Use additional authentications to autofill stored credentials data securely
    when you try to sign-in on web sites (e.g. Biometrics, Pattern, PIN and so
    on)
- **Bottom Toolbar Customization**
  - Re-arrange the items on the bottom toolbar to suit user's taste.
