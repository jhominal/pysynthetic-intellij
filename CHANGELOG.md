# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

# Change Log

## Unreleased

### Changed

 * Re-instated tweak of getter/setter auto-completion.

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
