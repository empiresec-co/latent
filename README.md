# Latent

e2ee group messaging with channels running over private nostr relay.

## Inspiration

The dir `/shared/src/commonMain/kotlin/co/empiresec` is inspired from [amethyst](https://github.com/vitorpamplona/amethyst/)

## Supported NIP's

### Necessary (v1)

- [ ] **Relay-based Groups** ([NIP-29](https://github.com/nostr-protocol/nips/blob/master/29.md
))
- [ ] Local Database

**core**

- [ ] Events / Relay Subscriptions (NIP-01)
- [ ] Private Direct Messages (NIP-17)
- [ ] DNS Address (NIP-05) (empiresec.co)
- [ ] Relay Authentication (NIP-42)
- [ ] HTTP Auth (NIP-98)
- [ ] Login with QR
- [ ] Classifieds (NIP-99)
- [ ] Private key encryption for import/export (NIP-49)

**messaging**

- [ ] Edits (NIP-37) (do not allow after 24 hrs)
- [ ] Event Deletion (NIP-09) (do not allow after 24 hrs)
- [ ] Replies, mentions, Threads, and Notifications (NIP-10)
- [ ] Expiration Support (NIP-40)
- [ ] Topics
- [ ] Pinned Messages
- [ ] Upward infinite scroll

**private relay**

- [ ] Relay Information Document (NIP-11)

### Neccessary (v2)

- [ ] OpenTimestamps Attestations (NIP-03)
- [ ] Image Capture in the app
- [ ] Video Capture in the app

**core**

- [ ] Gift Wraps & Seals (NIP-59)

**messaging**

- [ ] Audio Tracks (kind:31337)

**community funding**

- [ ] Wallet Connect API (NIP-47)
- [ ] Lightning Tips
- [ ] Zaps (NIP-57)
- [ ] Private Zaps
- [ ] Zap Splits (NIP-57)
- [ ] Zap Goals (NIP-75)
- [ ] Zapraiser (NIP-TBD)

**community**

- [ ] Calendar Events (NIP-52)
- [ ] Live Activities & Live Chats (NIP-53)
- [ ] Video Events (NIP-71)
- [ ] Moderated Communities (NIP-72)
- [ ] Hashtag Following and Custom Hashtags
- [ ] On-Device Automatic Translations
- [ ] Polls (NIP-69)

**UX**

- [ ] Events with a Subject (NIP-14)
- [ ] Reactions (NIP-25)
- [ ] Push Notifications (Unified Push)
- [ ] Badges (NIP-58)
- [ ] Draft Events
- [ ] Event Sets
- [ ] Markdown Support

**UI**

- [ ] Sensitive Content (NIP-36)
- [ ] Long-form Content (NIP-23)
- [ ] Image/Video/Url/LnInvoice Previews

### Nice to have's

- [ ] Generic Tag Queries (NIP-12)
- [ ] Custom Emoji (NIP-30)
- [ ] Event kind summaries (NIP-31)
- [ ] Torrents (NIP-35)
- [ ] User Status Event (NIP-38)
- [ ] External Identities (NIP-39)
- [ ] Workspaces
- [ ] Lists (NIP-51)
- [ ] Wiki (NIP-54)

### Maybe's

- [ ] Proof of Work Display (NIP-13)
- [ ] Proof of Work Calculations (NIP-13)
- [ ] Text Note References (NIP-27)
- [ ] Labeling (NIP-32)
- [ ] Parameterized Replaceable Events (NIP-33)
- [ ] Versioned Encrypted Payloads (NIP-44)
- [ ] Relay List Metadata (NIP-65)
- [ ] Embed events
- [ ] integrate nos2x

### Maybe Problems

- [ ] load new messages into msg_cache during getLatestMessage() rather than waiting for add-message
- [ ] load new messages into msg_cache during getLatestMessage() rather than waiting for add-message
- [ ] add a i18n solution (https://phrase.com/blog/posts/step-step-guide-javascript-localization/)
- [ ] add support for nos2x's relay list
- [ ] add support for ephemeral events (nip16) and use them to display "typing..." indicators
* [ ] add support for replaceable events (nip16) and use them to sync account-specific private data across devices

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

## Similar Projects

None

## Inspirations

its not native / js yuck - https://github.com/coracle-social/flotilla

https://github.com/damus-io/notedeck



## References

rust-egui performance - https://x.com/damusapp/status/1933255349425062332

https://github.com/rjaros/kilua/issues/36

https://github.com/varabyte/kobweb/discussions/704

