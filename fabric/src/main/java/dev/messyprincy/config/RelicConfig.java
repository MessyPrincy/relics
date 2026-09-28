package dev.messyprincy.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelicConfig {
    public int spawnIntervalSeconds = 300;
    public int spawnRadiusMin = 10;
    public int spawnRadiusMax = 40;
    public int relicLifeTimeSeconds = 600;
    public List<String> allowedDimensions = new ArrayList<>(List.of("minecraft:overworld"));
    public int commandPermissionLevel = 2;
    public Map<String, Integer> tiers = new HashMap<>(Map.of("bronze", 75, "silver", 20, "gold", 5));
}
