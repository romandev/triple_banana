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
