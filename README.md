<div align="center">
<a href="https://majnruj.cz/"><img src="https://github.com/ElFrod0/MajnrujClient/blob/master/.github/majnruj_client_logo.png?raw=true" alt="Purpur"></a>

## MAJNRUJ Client

[![MIT License](https://img.shields.io/github/license/ElFrod0/MajnrujClient?&logo=github)](LICENSE)
[![Join us on Discord](https://img.shields.io/discord/792768473964740608.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/K8Tt5F5STH)

MAJNRUJ Client (mClient in short) is a Fabric client mod, that is based on [PurpurClient](https://github.com/PurpurMC/PurpurClient). PurpurClient aims to fix various client-side bugs, while MAJNRUJ Client aims to provide new features. More on that below.

mClient is designed to work together with [Purpur](https://github.com/PurpurMC/Purpur) servers and mCore (Non open-source upstream of Purpur Server) servers. All the features are listed below. Keep in mind that most of the features can be used on any server type, but this mod is made especially to be used on mCore server(s).

This Client mod is not expected to be run alone, but as part of slightly modified Fabulously Optimized modpack. Available here: **NOT YET**
</div>

### Current Features in 1.20.4:
* Customizable mob passenger offsets.
* Adds bee counts inside beehives to debug screen.¹
* <s>Fancy Purpur-themed Loading Screen (can be disabled in the config)</s> <strong>Currently removed, will get "rebranded" soon.</strong>
* Displays Custom Enchantments instead of stripping them from the client.
* Removes the client-side limit of 255 levels.
* Fancy Window Title with random texts similar to MC's splash texts or Terraria's window title texts.
* Implementation of [@isXander's](https://github.com/isXander) mod [Main-Menu-Credits](https://github.com/isXander/main-menu-credits).
* Telemetry & Periodic telemetry (More on that below)² 
* Discord Rich Pressence using [@JnCrMx's](https://github.com/JnCrMx) [Discord-Game-SDK4J](https://github.com/JnCrMx/discord-game-sdk4j) Java bindings.³
* Improved chat with multiple tabs - General Chat, Trade Chat, DMs, etc. (**BETA!**) ³
  * Current "Better Chat" features:
    * Tabs for different chats.
    * Tabs specifically for DMs.
    * Markers if you have unread messages.
    * Compact design linked seamlessly with Vanilla configuration.
    * Feedback features to know where you're going to write.
  * There will be more features later on. It just takes time to implement. You can expect:
    * Longer history (> 100 messages) in each tab.
    * RegEx capabilities + detailed configuration for supporting **NEARLY EVERY** server.
    * Per-server pre-made configuration (via PRs soon).
* Built-in resource pack to make things internally easier.

¹ Only works when connected to [Purpur](https://github.com/PurpurMC/Purpur) servers.</br>
² Only works when connected to mCore servers.</br>
³ Works anywhere, but most features are available only when connected to mCore servers.

### Planned features
* Automated login to skip authentication process via command /login.
* Better Discord RPC implementation to invite players, join their parties, etc.
* Public open-source release of simplified mCore as Spigot/Paper plugin to support mClient players. Fabric-based release of mCore is not planned!
* Any ideas? Discord link below ;-)
## Contact
[![Join us on Discord](https://img.shields.io/discord/792768473964740608.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/K8Tt5F5STH)

Join us on [Discord](https://discord.gg/K8Tt5F5STH).

## Telemetry & Periodic Telemetry
It can of course be disabled in the configuration.
Telemetry is only sent when connected to mCore servers and data are sent through packets.</br>
### List of contained data in telemetry packets:
* Client's brand (eg. mClient v1.4)
* Game Version (eg. 1.20.4)
* Client modification status (boolean)
* Mods information (Count, List, SHA256 ModList Checksum)
* Resource Packs information (Count, List)
* CPU (Model, # ofCores, # ofThreads, Architecture)
* Currently used GPU
* RAM (Total, Available (via -Xmx Java arg), Used)
* Client's window resolution
* Operating System & it's version/build.
* Java Version
* Render Distance
* GUI Scale
* Graphics settings
* Chosen Language
### Periodic Telemetry contains:
* FPS (Limit, Lowest, Average, Highest)
* RAM (Usage, Available Total)
* Ping (Lowest, Average, Highest)
<p>This kind of data can be used for recommending users to change their settings, swap to dedicated GPU instead of using iGPU, improving modpack, etc.
<p>All data sent to mCore server may or may not be anonymized. We can check modlist for example, to see if you're trying to have unfair advantage or malicious intents.

## License
[![MIT License](https://img.shields.io/github/license/ElFrod0/MajnrujClient?&logo=github)](LICENSE)

All code is licensed under the MIT license, unless noted otherwise.