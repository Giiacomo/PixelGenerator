package me.giacomo.minecraft.pixelGenerator.helpers.enums;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.configuration.file.FileConfiguration;

public enum GeneratorInteractions {
    CAN_BREAK("can-break"),
    CAN_EXPLODE("can-explode"),
    CAN_MOVE_BY_PISTONS("can-move-by-pistons"),
    CAN_BURN("can-burn"),
    CAN_LAVA_DESTROY("can-lava-destroy"),
    CAN_ENTITIES_MOVE("can-entities-move");

    private final String configKey;

    GeneratorInteractions(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }

    public boolean getConfigValue() {
        return PixelGenerator.getInstance().getConfig().getBoolean("generators-interactions." + configKey, false);
    }
}
