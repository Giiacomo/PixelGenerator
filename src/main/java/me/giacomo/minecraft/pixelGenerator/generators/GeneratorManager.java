package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.TaskScheduler;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GeneratorManager {
    private static Map<Block, GeneratorBlock> generators = new HashMap<>();

    public static void addGenerator(GeneratorBlock generator, Player player) {
        if (!generators.containsKey(generator.getBlock())) {

            generators.put(generator.getBlock(), generator);
            TaskScheduler scheduler = getScheduleGenerationTask(generator);
            try {
                PixelGenerator.getInstance().getGeneratorDB().saveGenerator(generator);
            } catch (Exception e) {
                player.sendMessage("Couldn't save generator: " + e.getMessage());
                return;
            }
            try {
                PixelGenerator.getInstance().getGeneratorDB().loadGenerators().forEach(x-> player.sendMessage(x.toString()));
            } catch (Exception e) {
                player.sendMessage("Couldn't load generator: " + e.getMessage());
            }

            generator.setTask(scheduler.schedule());
        }
    }


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
