# Changelog

All notable changes to DollyMod are documented here.

Format based on [Keep a Changelog](https://keepachangelog.com/). Versioning follows [SemVer](https://semver.org/) (`mod_version` in `gradle.properties`).

## [1.2.0] - 2026-08-04

### Added
- Dolly runs to eat **cooked chicken** dropped on the ground (even if sitting); does not tame from floor food

## [1.1.1] - 2026-08-01

### Changed
- Replaced angry/growl audio with a custom Beagle howl (`dolly_howl.ogg`)
- Louder howl re-export from Audacity
- Subtitle text: “Dolly howls”

## [1.1.0] - 2026-08-01

### Changed
- Taming item is now **cooked chicken** (bones no longer tame Dolly)

### Added
- Immortality: Dolly ignores normal damage (creative / void can still remove her)

## [1.0.0] - 2026-08-01

### Added
- Initial Forge 1.20.1 / 47.4.0 mod (`dollymod`)
- Dolly entity (`dollymod:dolly`) — wolf-based tameable companion
- Spawn egg (creative Spawn Eggs tab); no natural world spawn
- Custom Blockbench Beagle model + texture
- Sound events (bark/pant/hurt/death still used vanilla wolf placeholders; growl later replaced in 1.1.1)
