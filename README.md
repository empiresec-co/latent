# Latent

e2ee group messaging with channels running over nostr.

## Spec

https://github.com/nostr-protocol/nips/blob/master/29.md

## Tech Stack

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop, ~~Server~~.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.
* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.
* ~~`/server` is for the Ktor server application~~
* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

Learn more about [Kotlin/Wasm](https://kotl.in/wasm/)… public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web). [GitHub](https://github.com/JetBrains/compose-multiplatform/issues). You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.
