package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class GeneratorManager {
    private static List<GeneratorBlock> generators = new ArrayList<>();

    public static void addGenerator(GeneratorBlock generator) {
        generators.add(generator);
        scheduleGenerationForGenerator(generator);
    }

    public void removeGenerator(GeneratorBlock generator) {
        generators.remove(generator);
    }


    public static void scheduleGenerationForGenerator(GeneratorBlock generator) {
        int interval = generator.getInterval();
        Bukkit.getConsoleSender().sendMessage(String.valueOf(interval));
        Bukkit.getScheduler().runTaskTimer(PixelGenerator.getInstance(), () -> { //new runnable replaced with lambda
            Bukkit.getConsoleSender().sendMessage(String.valueOf(interval));
            generator.generateItem();
        }, 0L, 20L * interval);
    }
}
