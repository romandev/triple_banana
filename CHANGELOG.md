# 2.01 @ 81.0.4044.117

## Chromium base version
- **81.0.4044.117**

## Change log
- Make the Secure DNS noti show only once (#412)
- Rebase 81.0.4044.117 (#411)
- Support dark mode on the toolbar editor (#400)
- Move the dark mode setting to UI setting on Banana Extension (#410)
- Fix a crash when randomly touching items on toolbar editor (#402)
- Ignore external intents for YouTube (#408)
- Make the secure dns noti to head-up noti only (#409)
- Replace menu word in ToolbarEditActivity. (#398)
- Add intent to the secure dns notification (#399)
- Fix the bug that the background play is stopped (#396)
- Implement notification for secure dns (#387)
- Add popup when user click back key (#384)
- Fix a bug that the home button is sometimes disappeared from top bar (#390)
- Fix a bug that the dark mode state is broken on some platform (#392)
- Fix the guide string in toolbar editor (#389)
- Change how to check the DnsOverHttps flag (#381)
- Use TextUtils.join instead of String.join (#379)
- Cherry pick "Use |typed_value| as a fallback if |value| is empty" (#383)
- Rebase 81.0.4044.111 (#380)
- Implement dark mode controller (#374)
- Implement dark mode GUI (#372)
- Implement secure DNS feature (#371)
- Cherry pick "Re-enable CECPQ2." (#370)
- Implement ui for secure DNS feature (#367)
- Fix a build error in FilterLoaderImpl (#363)
- Implement compressed filter extractor (#357)


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
