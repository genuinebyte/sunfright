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

Normal helmets give you resistance to the sunlight too. Every second you are
exposed to skylight greater than 3, your helmet takes one damage, or if you
don't have a helmet, you loose a heart. The table of helmet durability, taken
from the [Minecraft wiki][mcwiki-helmets], is listed below.

| Material     | Durability | Time Exposed to Sunlight  |
| ------------ | ---------- | ------------------------- |
| Leather      | 56         | 56 seconds                |
| Gold         | 78         | 1 minute, 18 seconds      |
| Iron         | 166        | 2 minutes, 46 seconds     |
| Chainmail    | 166        | 2 minutes, 46 seconds     |
| Turtle Shell | 276        | 4 minutes, 36 seconds     |
| Diamond      | 364        | 6 minutes, 4 seconds      |

[mcwiki-helmets]: https://minecraft.gamepedia.com/Helmet#Durability

### Notes
- Black glass can shield you from the sun if it is directly above you.
- Respawn helmets disappear when you are safe.
- You must have at least Fire Protection I on a helmet to stay safe from
  sunlight above level 3.
