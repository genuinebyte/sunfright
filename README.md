# Sunfright
Inspired by the [404 challenge][404], this is a Spigot plugin that restricts
your ability to be in sunlight.

[404]: https://www.minecraftforum.net/forums/minecraft-java-edition/seeds/298932-ironman-challenge-series-404

Every time you spawn, you're given a leather helmet with Curse of Binding and
Curse of Vanishing. This gives you some time to gather supplies before you can
shield yourself from the sunlight. If you try to take it off, it will vanish.
This leather helmet gives you around 5 minutes to gather everything you need to
live a good life underground, or return home.

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

### TODO:
[] Black Stained Glass to shield the sun
[] Fire Protection to act like unbreaking
[] Respawn helmet disappears when no longer exposed to sunlight.
[] Allow custom death messages when players burnt by the sun
[] Configure damage taken by sun
[] Option to adjust by difficulty
