package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class GeneratorManager {
    private static Map<Block, GeneratorBlock> generators = new HashMap<>();

    public static void addGenerator(GeneratorBlock generator) {
        if (!generators.containsKey(generator.getBlock())) {

            generators.put(generator.getBlock(), generator);
            TaskScheduler scheduler = getScheduleGenerationTask(generator);

            generator.setTask(scheduler.schedule());
        }
    }

    // Remove a generator and cancel its task
    public static void removeGenerator(GeneratorBlock generator) {
        if (generators.containsKey(generator.getBlock())) {
            generator.cancelTask();
            generators.remove(generator.getBlock());
        }
    }

    public static void removeGeneratorByBlock(Block block) {
        GeneratorBlock generator = findByBlock(block);
        if (generator != null) {
            removeGenerator(generator);
        }
    }

    public static GeneratorBlock findByBlock(Block block) {
        return generators.get(block);
    }

    public static TaskScheduler getScheduleGenerationTask(GeneratorBlock generator) {
        Runnable task = generator::generateItem;
        long delay = 0L;
        long interval = 20L * generator.getInterval();

        return new TaskScheduler(task, delay, interval);
    }

    public static boolean isGenerator(Block block) {
        return generators.containsKey(block);
    }
}
