# Git Hooks Gradle Plugin

This is a plugin that can automatically setup [Git hooks](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks) for your project, so you can store your hooks in the repo, and every developer will have the same hooks configured. Useful for setting up linter as a pre-commit hook.

## Usage

You will need to create a folder with your git hooks first. Please follow the [convention](https://git-scm.com/docs/githooks) for the hooks' naming. They are usually a simple shell scripts.

Then, in your project's `gradle/libs.version.toml` (version catalog) file, add the following lines:

```toml
[versions]
# ...
gitHooksPlugin = "1.0.0" # or pick the latest version available

[plugins]
# ...
gitHooks = { id = "eu.bambooapps.gradle.plugin.githook", version.ref = "gitHooksPlugin" }
```

Then, in your project's root `build.gradle.kts` file, add the following line to the `plugins` block:

```kotlin
alias(libs.plugins.gitHooks)
```

You'll need to specify the location of the folder with your hooks for the plugin to use. To do this, add the following block to your project's root `build.gradle.kts` file:

```kotlin
gitHooks {
    gitHooksDirectory.set(project.layout.projectDirectory.dir("git-hooks")) // or any other path where you put your hooks
}
```

After that, you can run `./gradlew installGitHooks` to configure hooks on your machine. If you want to automate this process a little, you can set up a dependency to `installGitHooks` task to the earliest stage of the build, or whatever task you like. For example:

```kotlin
tasks.named("clean") {
    dependsOn(":installGitHooks")
}
```
