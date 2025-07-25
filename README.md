<h1>
💬 <code>compact-chat</code>
<p></p>
<div>
    <a href="https://modrinth.com/mod/compact-chat">
        <img alt="modrinth" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg">
    </a>
    <img alt="fabric" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/fabric_vector.svg">
    <img alt="fabric" height="40" src="https://raw.githubusercontent.com/Hyperbole-Devs/vectors/8494ec1ac495cfb481dc7e458356325510933eb0/assets/compact/supported/neoforge_vector.svg">
</div>
</h1>

Compact Chat is a Minecraft mod that removes duplicate messages from your chat.

Minecraft versions 1.21.1 and higher are supported on both Fabric and NeoForge. Older Minecraft versions may be
supported in the future if there is demand for them! <sub>(no, there will not be a Forge
port)</sb>

This project uses to [ReplayMod/preprocessor](https://github.com/ReplayMod/preprocessor/) to support multiple Minecraft
versions and [EssentialGG/essential-gradle-toolkit](https://github.com/essentialgg/essential-gradle-toolkit) to make it
all easier to set up.

![A screenshot of the Minecraft chat, showing compact-chat compacting multiple messages into a single line](.github/screenshot.png)

## Compatibility with other mods

Compact Chat aims to be compatible with other mods, especially those which add timestamps to the start of messages.

By default, Compact Chat will not compact messages which start with a timestamp, but that behavior can be configured
by adjusting the "Ignore first characters count" setting within the configuration screen (requires
[ModMenu](https://modrinth.com/mod/modmenu) to open!).

![A screenshot of the Compact Chat configuration screen, showing the "Ignore first characters count" option set to '10'](.github/ignore-first-characters.png)

If this value is set, Compact Chat will ignore the first characters in the message up until the specified length.

For example, with the setting set to `10` and a message like so:

```
[20:19:00] <caoimheee> hello!
```

Compact Chat will see it as:

```
<caoimheee> hello!
```

## License

This project is licensed under the [MIT](./LICENSE) license.
