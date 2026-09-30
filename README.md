# Meßy's Relics

Open-source version of a vanilla plugin that I wrote for the DougDoug SMP server

## Config info
`enableMobLoot (default: true)`

Toggle the void traces from dropping from monsters

`voidTraceChance (default: 0.01)`

The chances of dropping a void trace

`enableVaultLoot (default = true)`

Toggle the relics keys from dropping from ominous vaults

`relicKeyChance (default = 0.05)`

The chances of dropping a relic key

`spawnIntervalSeconds (default = 300)`

The amount of time between relic spawns (It will be divided by the amount of online players in the allowed dimensions)

`spawnRadiusMin (default = 64)`

The minimum radius that a relic can spawn from a player in blocks

`spawnRadiusMax (default = 256)`

The maximum radius that a relic can spawn from a player in blocks (Note: a relic won't spawn in a unloaded chunk)

`relicLifeTimeSeconds (default = 600`

The lifetime of a relic in seconds

`allowedDimensions (default="minecraft:overworld")`

The worlds where your relics are allowed to spawn

`commandPermissionLevel (default = 2)`

The permission level for the commands (Add & Reload)

`tiers (default = "bronze", 75,
                    "silver", 20,
                    "gold", 5`)

The amount of tiers the relic loot will roll from with the according chances (always need to sum up to 100)

## Features
- Randomly spawning Relics, inspired by the Warframe's Relic mechanics
- Customizable loot-tables
- Customizable spawn rates and distances
- Relic keys dropping from ominous vaults, with customizable chances
- Void traces dropping from monsters, with customizable chances

## License
Meßy's Relics © 2026 by MessyPrincy is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/ 