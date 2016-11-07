# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

# Change Log

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
