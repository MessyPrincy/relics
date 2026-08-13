package dev.messyprincy.config;

import java.util.List;

public class RelicConfig {
    public int spawnIntervalSeconds = 300;
    public int spawnRadiusMin = 10;
    public int spawnRadiusMax = 40;
    public List<String> allowedDimensions = List.of("minecraft:overworld");
    public int commandPermissionLevel = 2;
}
