package dev.messyprincy.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelicConfig {
    public boolean enableMobLoot = true;
    public double voidTraceChance = 0.01;
    public boolean enableVaultLoot = true;
    public double relicKeyChance = 0.05;
    public int spawnIntervalSeconds = 300;
    public int spawnRadiusMin = 32;
    public int spawnRadiusMax = 256;
    public int relicLifeTimeSeconds = 600;
    public List<String> allowedDimensions = new ArrayList<>(List.of("minecraft:overworld"));
    public int commandPermissionLevel = 2;
    public Map<String, Integer> tiers = new HashMap<>(Map.of("bronze", 75, "silver", 20, "gold", 5));
}
