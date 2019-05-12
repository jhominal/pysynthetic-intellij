# PySynthetic plugin for IntelliJ and PyCharm

This project provides an IntelliJ plugin that assists in the usage of classes using members generated by the
[`PySynthetic`](https://pypi.python.org/pypi/pysynthetic) library.

The goal of this plugin is to provide:

 * Auto-completion of the names of generated members when using PySynthetic-using types;
 * Type information for the generated members, if available (via the use of `contract`);
 * Initializer editing assistance;

This plugin uses Parboiled for parsing contracts. Because of a dependency issue
(IntelliJ already has an asm.jar in its classpath, in version 5.0.3), we depend
on Parboiled version 1.1.7.

## Development setup

Build IDE version: 191.7141
Build Python Community Edition version: 2019.1.191.7141.2

 1. IDE setup

   * You need to have IntelliJ IDEA (Community Edition is enough), with the matching version
     * Ensure that you have the plugin development kit
   * Download the [Python Community Edition plugin](https://plugins.jetbrains.com/plugin/7322-python-community-edition)
     that matches the IDE version.
   * Open the pysynthetic-intellij project in IDEA
     * Configure an IntelliJ SDK version that matches the current IDE version
       * Open 'File/Project Structure...'
       * Add an IntelliJ Plugin development SDK if it does not exist, in 'Platform Settings/SDKs'
       * Configure the project to use the newly-created SDK, in 'Project Settings/Project'
     * In the SDK Library classpath, add `lib/python-ce.jar` (you will have to unzip the plugin zip file)

   Now, you should be able to successfully build the plugin using the entries in the "Build" menu.

 2. Run plugin with IntelliJ "run" command

   * The IntelliJ "run" command will automatically launch a version of IntelliJ, with a separate set of plugins.
   * The first time you run that IDE, ensure that the Python CE plugin is installed
   * Then open a Python project and check that everything works as expected

 3. Link to Python CE source code for debugging

   * Download the files from [IntelliJ CE public repository](https://github.com/JetBrains/intellij-community/),
     from the branch that matches the Python plugin version.
     * I recommend downloading a zip file instead of cloning, as there are a lot of files. (Just the zip is already a
       few hundred megabytes)
   * Add `python/src` to the SDK source paths
   * Warning: The downloaded source files are not an exact match for the compiled plugin. I do not know how to determine
     which commit matches a published plugin version.

## New version publication

 1. Create tag
 2. Compile module to zip file ("Build/Prepare plugin module for deployment...")
 3. Push tag and master branch to GitHub
 4. Put zip file on GitHub
 5. Put zip file on plugins.jetbrains.com
