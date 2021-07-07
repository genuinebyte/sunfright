# Inactive; Unmaintined

This plugin is no longer maintined. It might still work for your version of the game. If you plan to use it, I recommend testing it first on an unimportant server. I don't think it'd be able to cause much damage, but it's better to be safe.

# Sunfright
Inspired by the [404 challenge][404], this is a Spigot plugin that restricts
your ability to be in sunlight.

[404]: https://www.minecraftforum.net/forums/minecraft-java-edition/seeds/298932-ironman-challenge-series-404

Every time you spawn, you're given a leather helmet with Curse of Binding,
Curse of Vanishing, and Fire Protection. This gives you some time to gather
supplies before you can shield yourself from the sunlight, but if you try to
take it off, it'll vanish. This leather helmet gives you around 5 minutes to
gather everything you need to live a good life underground, or return home.

In order for the sun not to damage you, you must have equipped a helmet with
Fire Protection. If you were to take damage (according to the rules below), but
you have a Fire Protection helmet on, the helmet takes half the damage you would
be dealt and you don't take any damage. There is a table below listing the
types of helmets, their durability, and the time it would take for this helmet
to break on the default damage value. Data is pulled from the
[Minecraft Gamepedia page on Helmets][mcwiki-helmets].

| Material     | Durability | Time Exposed to Sunlight  |
| ------------ | ---------- | ------------------------- |
| Leather      | 56         | 56 seconds                |
| Gold         | 78         | 1 minute, 18 seconds      |
| Iron         | 166        | 2 minutes, 46 seconds     |
| Chainmail    | 166        | 2 minutes, 46 seconds     |
| Turtle Shell | 276        | 4 minutes, 36 seconds     |
| Diamond      | 364        | 6 minutes, 4 seconds      |

[mcwiki-helmets]: https://minecraft.gamepedia.com/Helmet#Durability

## Download
You can get the latest build from [**Curseforge**][curseforge], [**SpigotMC**][spigot], or on the
[**GitHub releases page**][github-releases].

[curseforge]: https://www.curseforge.com/minecraft/bukkit-plugins/sunfright
[spigot]: https://www.spigotmc.org/resources/sunfright.86454
[github-releases]: https://github.com/genuinebyte/sunfright/releases

## Configuration
Currently there are only three configuration options, but more will be made
available as the plugin evolves. If you think something should be configurable,
please [open an issue][issue-tracker]!

[issue-tracker]: https://github.com/genuinebyte/sunfright/issues

**world**  
Default: `world`

This is the game world you would like the plugin to operate in.

**damagePerSecond**  
Default: `2`

The amount of damage you want the player to take from the sun. If they're
wearing a helmet with Fire Protection, it will take half of this damage rounded
up to the nearest whole number. One damage is equal to 1 half-heart.

**enableRespawnHelmet**  
Default: `true`

Whether or not you get a helmet that protects you from the sun on respawn. It's
setup in such a way that the helmet can only last 5 minutes in the sun and will
disappear the instant you're safe from the sun.

## Damage Conditions
You will be damaged if you are in direct sunlight and not under a solid block
(or black stained glass) and the time falls in any of these ranges:

| Weather Condition | Time Start/End | Clock Start/End |
| ----------------- | -------------- | --------------- |
| Clear             | 13026 - 22973  | ![](docs/clear_safe.png) |
| Raining           | 12733 - 23266  | ![](docs/stormy_safe.png) |
| Thunder Storm     | 12299 - 23699  | ![](docs/thunder_safe.png) |

In addition to these conditions, your helmet may not take damage if you have
a Fire Protection level higher than I. If you do, then the extra Fire Protection
levels act as Unbreaking. So if you have Fire Protection IV, then it is
equivalent to Unbreaking III and the sun has a 30% chance to not damage your
helmet. Fire Protection III has a 27% chance to not damage, and II a 20%.
