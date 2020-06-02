# Sunfright
[todo.sr.ht][tickets]

[tickets]: https://todo.sr.ht/~genbyte/sunfright

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

## Damage Conditions
You will be damaged if you are in skylight greater than 3 and it is not during
this time of day.

| Weather Condition | Time Start/End | Clock Start/End |
| ----------------- | -------------- | --------------- |
| Clear             | 13026 - 22973  | ![](docs/clear_start_safe.png) ![](docs/clear_end_safe.png) |
| Raining           | 12733 - 23266  | ![](docs/stormy_start_safe.png) ![](docs/stormy_end_safe.png) |
| Thunder Storm     | 12299 - 23699  | ![](docs/thunder_start_safe.png) ![](docs/thunder_end_safe.png) |

### Notes
- Respawn helmets disappear when you are safe.
- You must have at least Fire Protection I on a helmet to stay safe from
  sunlight above level 3.
