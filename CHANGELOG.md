# 3.02 @ 83.0.4103.106

## Chromium base version
- **83.0.4103.106**

## Change log
- Disable SyncAdapter (#480)
- Refactor FilterLoader for adblocker (#479)
- Show black lives matter depending on remote config (#478)
- Add terminate button to kill browser (#477)
- Add link for black lives matter (#473)
- Blacklist the SurfaceControl on ARM64 (#474)
- Add a phrase for black lives matter (#471)
- Rebase 83.0.4103.106 (#472)
- Replace adblock filter version string (#470)
- Fix a bug that the filter is not updated (#469)
- Fix a crash when YouTube play on Android L (#467)
- Improve DPI blocker algorithm (#465)
- Fix a build error in release build (#464)
- Rebase 83.0.4103.101 (#463)
- Fix a bug that the SecureDNS head-up noti doesn't show on N- OS (#461)
- Fix a bug that the SecureDNS head-up noti doesn't disappear on N- OS (#460)
- Disable AImageReader for ARM64 build on all Android versions (#457)
- Reduce filter update interval (#455)
- Display Adblocker filter version on ExtensionFeatures page (#454)
- Rebase 83.0.4103.96 (#453)
- Display correct app version name (#452)
- Refactor update_strings command to cover multiple *strings.xml (#450)


# 3.01 @ 83.0.4103.60

## Chromium base version
- **83.0.4103.60**

## Change log
- A crash occur whenever dark mode on or off. (#447)
- Tab switcher button isn't shown on top toolbar (#444)
- Rename icon tint color resource (#448)
- Implement DPI(Deep Packet Inspection) blocker (#442)
- Fix a bug that the image reader is enabled on ARM64 (#441)
- Fix a bug that the app icon and app name are displayed incorrectly (#438)
- Fix a bug password update prompt always appears (#439)
- Rebase 83.0.4103.60 (#427)
- Disable sync related permissions (#429)
- Fixing a zip path traversal vulnerability (#424)


# 2.03 @ 81.0.4044.138

## Chromium base version
- **81.0.4044.138**

## Change log
- Fixing a zip path traversal vulnerability (#424)


# 2.02 @ 81.0.4044.138

## Chromium base version
- **81.0.4044.138**

## Change log
- Change the new feature icon size (#422)
- Handle the new feature icon if applicaiton is updated as a new version (#421)
- Store application version name (#420)
- Implement HSTS to enforce https (#416)
- New toolbar button is not displayed. (#415)


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
