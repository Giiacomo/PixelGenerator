package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.visibility;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;

public enum Ranges {
    VISIBILITY_RANGE("visibility-range"),
    ACTIVATION_RANGE("activation-range");

    private final int range;

    Ranges(String rangeConfig) {
        String rangePath = "generator-ranges.";
        this.range = PixelGenerator.getInstance().getConfig().getInt(rangePath + rangeConfig);
    }

    public int getRange() {
        return range;
    }
}
