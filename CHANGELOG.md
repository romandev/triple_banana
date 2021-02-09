# 6.05 @ 88.0.4324.152

## Chromium base version
- **88.0.4324.152**

## Change log
- Fix a bug that the Secure DNS migration is sometimes failed (#1109)


# 6.04 @ 88.0.4324.152

## Chromium base version
- **88.0.4324.152**

## Change log
- Fixed toolbar editor layout issue (#1107)
- Fix a blinking bug in the banana extension settings (#1106)
- Fixed wrong update button color (#1104)
- Fix a crash when clicking search button after dark mode change (#1103)
- Fix a bug that the QR code scan doesn't work if there are no tab (#1102)


# 6.03 @ 88.0.4324.152

## Chromium base version
- **88.0.4324.152**

## Change log
- Rebase 88.0.4324.152 (#1100)
- Ignore sentinel file mechanism (#1099)
- Fix a crash in debug build (#1098)
- Implement BananaStatusBarController (#1094)
- Implement "Data clear suggestion" dialog (#1063)
- Fix a bug that the Secure DNS data migration doesn't work correctly (#1097)
- Add "Reset adblock filter" preference (#1095)
- Add icon to Banana Extension preference (#1087)
- Adjust the animation time to show the QuickMenu (#1076)
- Handle CECPQ2 feature flag at runtime without restarting process (#1093)
- [QuickMenu] Increase touch area for top icon (#1092)
- Use base::flat_map in base::FeatureList. (#1090)
- Add an exception to be execluded during update_upstream command (#1088)
- Add getLastTrackedFocusedActivity api to applitcation utils (#1084)
- Add browser settings preference in banana extension (#1080)
- Fix a release build error (#1079)
- Change from observer method to query in ButtonStateManager (#1071)
- Disable bytecode_rewriter_target for androidx_fragment (#1075)
- Renewal icons/labels of toolbar/quick menu (#1074)
- Disable "Touch to fill" feature (#1073)
- Fix a crash when a user clicks the secure dns preference (#1070)
- Implement translate button on quick menu (#1060)
- Rebase 88.0.4324.93 (#1059)
- Replace clear data with user interface on quick menu (#1058)
- Change the order of the icons in QuickMenu (#1057)
- Separate the dark mode preference from the user interface (#1056)
- Prevent quick menu dialog to be shown multiple times (#1045)
- [QuickMenu] Add padding for bottom layout for QuickMenu (#1046)
- Add button actions for AdBlock and Secure DNS in quick menu (#1044)
- Remove quick settings from banana extension settings (#1043)
- Separate the adblock preferences from the advanced features (#1042)
- Implement BananaButtonStateProvider (#1035)
- [Toolbar] Add media button for bottom toolbar (#1030)
- Fix a build error in ButtonInfoStorageImpl (#1034)
- Fix a bug that the play state is not updated in media remote (#1033)
- Adjust the order of the button list in quick menu (#1032)
- Implement BananaButtonStateManager (#1014)
- [QuickMenu] Add bottom buttons for QuickMenu (#1027)
- [Toolbar] Fix QuickMenu button on BottomToolbar (#1025)
- Implement QuickMenuTopButton (#999)
- Fix a crash when a user clicks quick menu button (#1024)
- Remove QuickMenu- prefixes from the class names (#1023)
- Make the ViewModel.addListener take WeakReference (#1022)
- Refactor quick menu module (#1019)
- Remove QuickMenuButton (#1020)
- [QuickMenu] Fixed dark mode issue on QuickMenuDialog (#1015)
- [QuickMenu] Support dialog rotation (#1017)
- [QuickMenu] Reduction vertical length of QuickMenu (#1016)
- Update quick menu bottom layout (#1000)
- Update quick menu top layout (#996)
- Correct the memory ownership in quick_menu module (#998)
- Fix a bug that the quick menu doesn't work in debug build (#997)
- Use Runnable instead of QuickMenuActionProvider.Action (#994)
- Implement QuickMenuStorage and use it (#993)
- Replace QuickMenuNewButton to QuickMenuMiddleButton (#989)
- Implement new button for quick menu (#987)
- Remove MockQuickMenuViewImpl (#988)
- Use new generic Model class in quick_menu module (#985)
- Implement generic Model class (#984)
- Change default color for QuickMenuButton (#982)
- Bind QuickMenuViewModel to QuickMenuView (#977)
- Rebase 88.0.4324.51 (#980)
- Fix a build error in 64bit APK (#979)
- Refactor YouTube quality options dialog (#957)
- Implement QuickMenuStorageModel (#959)
- Remove QuickMenuGridLayout (#963)
- [WIP] Implement QuickMenuLayout (#954)
- Implement quick menu buttons from bottom toolbar (#951)
- Factor out QuickMenuViewModelData from QuickMenuViewModel (#956)
- Adjust text size of toolbar editor description (#953)
- Enable the download button only when external download option is enabled (#952)
- Adding QuickMenuDialog that appears at the bottom (#948)
- Implement QuickMenuActionProvider to support QuickMenu (#946)
- Make the infobar hidden during fullscreen video (#950)
- Add the Secure DNS preference on privacy and security (#944)
- Set the Secure DNS mode when feature_secure_dns is on (#943)
- Implement the Secure DNS bridge to access config (#918)
- Rebase 88.0.4324.39 (#945)
- Disable closed caption for YouTube feature by default (#942)
- Remove show() and dismiss() from QuickMenuView (#941)
- Implement skeleton codes for QuickMenu (#939)
- Change the behavior related to upsteram Secure DNS (#903)
- Implement playback UI on Media Remote (#902)
- Implement MediaRemoteOptionDialog and apply it for YouTube caption (#908)
- Implement YouTubePlayerController and CommandBuilder (#907)
- Implement to control playback rate on media remote (#898)
- Introduce isPipSupported() in MediaRemoteViewModel and use it (#899)
- Add QRCode feature for bottom toolbar (#895)
- Fix a crash for add to homescreen toolbar button (#880)
- Implement closed caption UI on media remote (#894)
- Implement video download API for media command processor (#893)
- Make the video download button show only if the video is downloadable (#891)
- Implement video download UI on media remote (#890)


# 6.02 @ 87.0.4280.66

## Chromium base version
- **87.0.4280.66**

## Change log
- Change check logic whether it is first install (#876)
- Disable "Secure DNS" menu on Privacy Settings (#875)


# 6.01 @ 87.0.4280.66

## Chromium base version
- **87.0.4280.66**

## Change log
- Rebase 87.0.4280.66 (#871)
- Update strings for 87.0.4280.60 (#869)
- Fix a crash when enabling "Tab Groups" feature (#868)
- Rebase 87.0.4280.60 (#866)


# 5.06 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Missing internal patches


# 5.05 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Reland: Use system linker instead of crazy linker since N+ OS (#862)


# 5.04 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Fix a bug that the state isn't synced between dark mode button and pref (#860)
- Introduce disable_library_rename flag and use it (#859)


# 5.03 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Disable chromium crazy linker for 32bit binary (#856)


# 5.02 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Disable "Touch to fill" feature by default (#854)
- Refactor touch_to_fill_view_impl.(h|cc) (#852)
- Fix a bug that "Touch to fill" UI doesn't work when switching dark mode (#851)
- Fix a bug that the dark mode switch is not synced (#849)
- Fix a bug that the BrowserLock is not working when app restarts (#848)
- Fix a crash when launching application with 32bit apk (#847)
- Extends RestartPreference for Media Features (#845


# 5.01 @ 86.0.4240.185

## Chromium base version
- **86.0.4240.185**

## Change log
- Fix a bug that the android lint doesn't work
- Implement subresource filter update UI (#839)
- Update title and summary for data reduction mode (#838)
- Extends RestartSwitchPreference for bottom toolbar preference (#837)
- Set the title of settings screens (#835)
- Change feature name and summary (#834)
- Reorganize the quick settings on Banana Extension (#833)
- Add data reduction mode to advanced features (#831)
- Rename "Privacy" and "Appearance" with new things (#829)
- Fix a crash that the "Privacy" menu on Settings (#827)
- Merge NewExtensionFeatures with ExtensionFeatures (#826)
- Fix a bug that the quick settings aren't synced with the detail settings (#825)
- Implement SecurityLevelChecker for SecureLogin and BrowserLock (#824)
- Enable NewExtensionFeatures settings by default (#823)
- Factor out custom preferences from ExtensionFeatures (#818)
- Rename FilterLoader with RulesetLoader (#821)
- Rebase 86.0.4240.185 (#822)
- Refactor FilterLoader to support the force ruleset update (#820)
- Introduce successCallback parameter to Banana/CakeSubresourceFilter (#819)
- Remove setFilterVersion() method in VersionInfo (#817)
- Disable NewExtensionFeatures settings (#815)
- Fix a bug that the adblock version is not displayed (#814)
- Fix a crash when running the new banana extension features (#812)
- Implement new extension preferences (#805)
- Fix a build break in clear build (#808)
- Implement LongClickableSwitchPreference and use it for adblock pref (#804)
- Move the built-in filter version info to BUILD.gn (#801)
- Fix a build break in AppMenuDelegate (#802)
- Change the order of available quality options (#798)
- Fix a bug that the new feature icon is not displayed sometimes (#800)
- Adding "clear brwosing data" logic on terminate (#793)
- Fix a bug that the subresource filter's ruleset data is downgraded (#799)
- Reimplement subresource filter using JNI instead of mojom IPC (#797)
- Add options to select quality on media remote (#777)
- Replace CLEAR_BROWSING_DATA with CLEAR_DATA (#792)
- Add clear browsing data logic (#778)
- Enable TabSwitcher on bottom toolbar by default (#789)
- Implement external download manager supports (#788)
- Implement YouTube quality options skeleton code (#786)
- Implement DownloadInterceptor (#787)
- Rebase 86.0.4240.110 (#785)
- Add new version the function to check YouTube url (#784)
- Move function to check youtube domain to common class (#781)
- Fix a bug the upstream toolbar appears for a while (#783)
- Add switch preference for clearing browsing data (#779)
- Add BananaJavaScriptCallback interface (#780)
- Add Tab Switcher button on the bottom toolbar (#773)
- Change the icon resource for clear browsing data (#775)
- Add close current tab button (#770)
- Add clear browsing data preference (#771)


# 4.07 @ 85.0.4183.127

## Chromium base version
- **85.0.4183.127**

## Change log
- Fix a bug that the browser lock is triggered even if it is disabled (#764)
- Fix a bug that the subresource_filter doesn't work after app update (#762)
- Revert "Remove AtMeGame service (#759)" (#765)


# 4.06 @ 85.0.4183.127

## Chromium base version
- **85.0.4183.127**

## Change log
- Remove AtMeGame service (#759)


# 4.05 @ 85.0.4183.127

## Chromium base version
- **85.0.4183.127**

## Change log
- Reorder media features (#757)


# 4.04 @ 85.0.4183.127

## Chromium base version
- **85.0.4183.127**

## Change log
- Fix a bug that the 32bit binary doesn't install (#755)
- Update new built-in filter version (1.0.0.00003) (#754)


# 4.03 @ 85.0.4183.127

## Chromium base version
- **85.0.4183.127**

## Change log
- Fix a bug when a user click home button on media remote on N- OS (#752)
- Fix the update_strings command bug (#751)
- Rebase 85.0.4183.127 (#750)
- Remove unnecessary upstream changes (#749)
- Enable use32bitAbi on 64bit build (#748)
- Reduce mojo calls on media event dispatcher (#743)
- [mojo] [bindings] Add sync response support for Java bindings (#740)
- Fix a bug that the media remote is not working for basic video (#738)


# 4.02 @ 85.0.4183.101

## Chromium base version
- **85.0.4183.101**

## Change log
- Rebase 4.01 @ 85.0.4183.101 (#736)
- Refactor media remote feature in engine side (#735)
- Reduce the touch bounds to prevent unexpected gesture detection (#733)
- Introduce pauseForAMoment() method to BrowserLock (#734)
- Make the authenticate() method take parent activity (#732)
- Remove legacy activity based backend implementation (#731)
- Implement KeyguardBackend and use it (#730)
- Use finishAndRemoveTask() instead of moveTaskToBack() (#729)
- Revert "Introduce shutdown() API in BananaApplicationUtils (#720)" (#728)
- Remove BiometricPromptActivity (#727)
- Add authentication description for browser lock (#713)
- Fix a crash when entering fullscreen mode with MediaRemote (#716)
- Introduce ApplicationStatusTracker.reset() and use it (#723)
- Implement BiometricPromptBackend and use it (#722)
- Fix a build error (#721)
- Introduce shutdown() API in BananaApplicationUtils (#720)
- Refactor ApplicationStatusTracker (#719)
- Fix old GitHub repository URI (#718)
- Supply browser lock summary description (#710)
- Replace darkmode button instead of share button (#708)
- Restore removed license (#705)
- Remove the code related to black live matter (#704)
- Show toast when try to enter pip mode not in playing (#706)
- Fix a crash when entering fullscreen without blink features flag (#699)
- Reformat upstream patches (#697)
- Fix a bug that the media remote is sometimes not closed (#696)
- Fix two critical warnings (#695)
- Check whether device is secured or not befor try to authenticate (#693)
- Fix a bug that the media remote buttons don't work on some websites (#694)
- Disable feed suggestions in new tab page (#689)
- Fix a bug that BrowserLock starts after authentication for SecureLogin (#688)
- Fix a bug that the unexpected ApplicationStatus.FOREGROUND is triggered (#682)
- Fix a bug that the session doesn't keep for 60 seconds (#680)
- Sync BrowserLock feature with security level (#684)
- Implement VerticalSeekBar and replace the rotated SeekBar (#683)
- Remove unused resource file in BUILD.gn (#679)
- Adjust the controls background color (#678)
- Fix a bug that the view state is not updated after re-layout (#672)
- Fix a bug that the controls is sometimes hidden too quickly (#676)
- Make the fullscreen toast not show when the media remote is enabled (#674)
- Make the content view re-layout whenever the device rotation is changed (#670)
- Refactor media_remote module (#669)
- Fix a bug that the username is not stored on naver.com (#671)
- Apply session concept for BrowserLock (#653)
- Make the pip button invisible on remote view (#660)
- Implement RotationManager class to detect device rotation state (#662)
- Enable pip mode even disablePictureInPicture attribute (#659)
- Fix a bug that the play() doesn't work on media remote if autoplay off (#657)
- Fix a bug that the play state is not updated (#656)
- When video state is in a waiting state, playback smoothly (#652)
- Change media remote view layout structure (#647)
- Update filter server URL and and new built-in filter version (#654)
- Fix a bug that the media in playing is paused when entering background (#655)
- Add a switch option on settings for media remote feature (#651)
- Refactor the initCommandLine() method (#650)
- Rename remote_control with media_remote (#649)
- Show navbar when the controls are visible (#635)
- Add overflow checks in onPositionChanged() (#648)
- Fix a bug that the the remote control is gone suddenly during waiting (#639)
- Fix a bug that the save password prompt isn't shown after login failure (#641)
- Reformat upstream patches (#640)
- Rebase 85.0.4183.81 (#636)
- Fix a bug that `update_chromium` command couldn't fetch the remote tags (#634)
- Change the lock state icon on RemoteControlView (#632)
- Fix a bug navbar is shown when back to fullscreen (#630)
- Support for auto hiding controls after 5 seconds (#625)
- Fix a crash on Naver TV with remote control service (#627)
- Make BrowserLock class as singleton(#565) (#629)
- Fix a bug the remote control view is not shown after PIP mode (#622)
- Remove unnecessary test buttons on RemoteControlView (#623)
- Add ripple effect when a user double-clicks (#616)
- Implement metadata update and seeking on remote control (#618)
- Fix a crash when entering remote control service (#617)
- Make the play/pause buttons toggled (#615)
- Implement additional media events on MediaEventDispatcher (#614)
- Invert the mute button state (#613)
- Implement mute on RemoteControl service (#603)
- Make the gesture detector trigger the only one gesture event at a time (#602)
- Fix colors of volume/brightness controls on remote control view (#601)
- Implementation of vertical seekbar for volume and brightness (#595)
- Implement double tap gestures for forward/backward operations (#600)
- Fix sensitivity for gesture detection (#599)
- Block background when authencicate BrowserLock (#588)
- Introduce ViewModel.reset() to reset the model data (#598)
- Implement seekTo in SeekBar and on gesture event (#586)
- Implement lock button on remote control (#594)
- Change MediaController according to MediaCommandProcessor (#593)
- Use runMediaCommand instead of BananaMediaCommandProcessor (#592)
- Use Keygaurd as a fallback when the biometric sensor is unavailable (#591)
- Fix a bug that the authentication callback is duplicately called twice (#590)
- Make the sizes of the icons on remote control view smaller (#587)
- Use androidx.biometric.BiometricPrompt instead of android platform APIs (#580)
- Add androidx.biometric module (#579)
- Fix to unexpected BrowserLock is not shown in case of setting DarkMode (#577)
- Make BrowserLock start/stop no need to restart (#576)
- Implement show/hide controls feature on remote control view (#575)
- Implement close button on remote control service (#574)
- Fix a bug system ui was displayed incorrectly on remote control (#572)
- Support pip mode on remote control service (#571)
- Fix the fullscreen related issues for RemoteControl service (#570)
- Change the return type of getting media volume (#564)
- Implement GestureDetector for remote control (#551)
- Fix build error (#550)
- Disable orientation change event if fullscreen mode entered (#547)
- Fix a bug that the RemoteControlView isn't present even in fullscreen (#545)
- Fix a crash when launching browser with 32bit binary (#543)
- Fix a bug that OnExitedVideoFullscreen() isn't sometimes detected (#544)
- Implment BrowserLock and setting menu using ApplicationStatusTracker (#541)
- Fix a build error (#540)
- Implement banana pip controller (#538)
- Implement media control view layout (#537)
- Fix a bug that remote control view's play button doens't work (#536)
- Fix a launch crash on some OS (#533)
- Change the dialog style when entered fullscreen mode (#534)
- Implement ApplicationStatusTracker as general class (#530)
- Implement CountryCodeUtil and apply it on AtMeGame service for India (#531)
- Implement bottom toolbar button for AtMeGame service (#529)
- Remove "Sync and Services" link text on PrivacySettings (#528)
- Implement "Translate" switch on banana settings (#527)
- Implement volume operations for remote control (#526)
- Implement rotate operation for remote control (#525)
- Introduce AudioUtil to control volume (#524)
- Add remote control view model (#523)
- Implement media autoplay enable/disable feature (#519)
- Implement exit fullscreen for banana remote control (#522)
- Refactor MockRemoteControlView (#521)
- Refactor RemoteControlService (#520)
- Update mock remote control view (#517)
- Implement video operations for remote control (#516)
- Implement play/pause operations for remote control (#514)
- Introduce BrightnessUtil to adjust screen brightness easily (#513)
- Implement mock remote control view (#512)
- Implement remote control skeleton code (#509)
- Disable "Sync and Services" link on PrivacySettings (#507)
- Rebase 85.0.4183.47 (#506)
- Fix a crash in 64bit binary after rebase m84 (#500)
- Change out target directory depending on architecture type (#499)
- Fix the `build` command bug that `--64bit` option is not working (#498)
- Fix a break during update_chromium running (#495)
- Refactor interfaces.h to support Blink mojom binding (#485)
- Rebase 84.0.4147.84 (#483)


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
