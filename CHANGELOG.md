# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

# Change Log

## Unreleased

### Changed

 * Update plugin declaration no longer to declare a maximum IDEA platform version

## 0.6.3

### Changed

 * Update plugin declaration for IDEA platform version 183

## 0.6.2

### Changed

 * Update plugin declaration for IDEA platform version 182
 * Use standard Java exception `UnsupportedOperationException` instead of Apache Commons `NotImplementedException`

## 0.6.1

### Changed

 * Update plugin declaration for IDEA platform version 181

## 0.6.0

### Added

 * Support for IntelliJ IDEA platform version 173

### Removed

 * Support for IntelliJ IDEA platform version 172

## 0.5.0

### Added

 * Support for IntelliJ IDEA platform version 172
 * README file + IDE files for easier project setup

### Changed

 * Updated parboiled version to 1.1.7 (to be in sync with version of asm shipped in IntelliJ IDEA)

### Removed

 * Support for IntelliJ IDEA platform versions below 172

## 0.4.0

### Added

 * Show warning when a synthetic constructor is called without filling a member
   that has no specified default, and cannot accept `None`.

### Changed

 * Re-instated tweak of getter/setter auto-completion.
 * Most contract expressions that do not involve variables or numpy
   are now supported, and return useful auto-completion results.

## 0.3.2

### Changed

 * Fix bugs introduced by 0.3.1 (many erroneous warnings in IDE).
 * Revert tweak of getter/setter auto-completion.

## 0.3.1

### Changed

 * Synthetic constructor parameter order fixes:
   * Fixed incorrect duplicate parameters when some synthetic members define arguments with matching names.
   * Fixed incorrect ordering of parameters when `**kwargs` are present.
 * Tweaked auto-completion of getters/setters to be closer to native method auto-completion.

## 0.3.0

### Added

 * Support for synthetic initializers:
   * Full Parameter information in the IDE is supported if the `__init__` method is re-declared.
   * Otherwise, support is limited to argument name completion.
 * Full Parameter information for getters and setters in the IDE.

## 0.2.0 - 2016-11-05

### Added

 * Support for auto-completion based on the value of the `contract` argument.
 Only a limited set of contracts is currently supported.

### Changed

 * Enhanced performance due to caching of synthetic type information.
 * Icons added to generated members' autocompletion.

## 0.1.0 - 2016-11-02

 * Initial version.
 * Support for auto-completion of the names of generated member.
