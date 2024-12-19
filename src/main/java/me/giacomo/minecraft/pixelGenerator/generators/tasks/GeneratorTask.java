package me.giacomo.minecraft.pixelGenerator.generators.tasks;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;

public class GeneratorTask implements Runnable {
    private final AbstractGeneratorBlock generatorBlock;
    private final long intervalMillis;
    private final int timeToReactivateSeconds;
    private final boolean canDisableGenerators;
    private final boolean rememberTimeRemaining;

    private long lastRunTime;
    private long timeRemaining;
    private long nextCheckTime;
    private boolean isGeneratorOn;

    public GeneratorTask(AbstractGeneratorBlock generatorBlock) {
        this.generatorBlock = generatorBlock;
        this.intervalMillis = generatorBlock.getInterval() * 1000L;
        this.timeToReactivateSeconds = PixelGenerator.getInstance().getConfig().getInt("generator-ranges.time-to-reactivate");
        this.canDisableGenerators = PixelGenerator.getInstance().getConfig().getBoolean("generator-ranges.out-of-range-disable");
        this.rememberTimeRemaining = PixelGenerator.getInstance().getConfig().getBoolean("generator-ranges.remember-time-remaining");

        this.lastRunTime = System.currentTimeMillis();
        this.timeRemaining = intervalMillis;
        this.nextCheckTime = 0;
        this.isGeneratorOn = false;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        if (now > nextCheckTime) {
            updateCheckTime(now);
            if (!isGeneratorActive()) {
                handleGeneratorOff();
                return;
            }
        } else if (!isGeneratorOn) {
            return;
        }

        if (!shouldRememberTimeRemaining(now)) {
            resetTimeRemaining(now);
            return;
        }

        updateTimeRemaining(now);
        if (timeRemaining <= 0) {
            generatorBlock.generateItem();
            timeRemaining = intervalMillis;
        }

        updateHologram();
    }

    private void updateCheckTime(long now) {
        nextCheckTime = now + timeToReactivateSeconds * 1000L;
        isGeneratorOn = generatorBlock.getVisibility().isAnyoneInGenerationRange();
    }

    private boolean isGeneratorActive() {
        return !canDisableGenerators || isGeneratorOn;
    }

    private void handleGeneratorOff() {
        if (generatorBlock.getHologram() != null) {
            generatorBlock.setHologramReactivatingMessage();
        }
    }

    private boolean shouldRememberTimeRemaining(long now) {
        return rememberTimeRemaining || lastRunTime >= nextCheckTime - 10 * 1000L;
    }

    private void resetTimeRemaining(long now) {
        timeRemaining = intervalMillis;
        lastRunTime = now;
    }

    private void updateTimeRemaining(long now) {
        long elapsedTime = now - lastRunTime;
        timeRemaining = Math.max(0, timeRemaining - elapsedTime);
        lastRunTime = now;
    }

    private void updateHologram() {
        if (generatorBlock.getHologram() != null) {
            generatorBlock.updateHologramText(String.valueOf(timeRemaining / 1000));
        }
    }
}
