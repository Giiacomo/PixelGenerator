package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.db.GeneratorDB;
import me.giacomo.minecraft.pixelGenerator.helpers.TaskScheduler;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GeneratorManager {
    private static Map<Block, GeneratorBlock> generators = new HashMap<>();

    public static void loadAllGenerators() throws SQLException {

        List<GeneratorDB.Generator> savedGenerators = PixelGenerator.getInstance().getGeneratorDB().loadGenerators();
        savedGenerators.forEach(generator -> {
            World world = Bukkit.getWorld(generator.getWorld());
            Block block = world.getBlockAt(generator.getX(), generator.getY(), generator.getZ());
            Material material = Material.getMaterial(generator.getMaterial());
            int interval = generator.getInterval();
            int quantity = generator.getQuantity();

            GeneratorBlock generatorBlock = new GeneratorBlock(block, material, interval, quantity);
            addGenerator(generatorBlock);

        });

    }


    public static void addGenerator(GeneratorBlock generator) {
        if (!generators.containsKey(generator.getBlock())) {

            TaskScheduler scheduler = getScheduleGenerationTask(generator);
            try {
                PixelGenerator.getInstance().getGeneratorDB().saveGenerator(generator);
                generators.put(generator.getBlock(), generator);
            } catch (Exception e) {
                return;
            }

            generator.setTask(scheduler.schedule());
        }
    }


    public static void removeGenerator(GeneratorBlock generator) throws SQLException {
        if (generators.containsKey(generator.getBlock())) {
            generator.cancelTask();
            generators.remove(generator.getBlock());
            PixelGenerator.getInstance().getGeneratorDB().removeGenerator(generator);
        }
    }

    public static void removeGenerator(Block block) {
        GeneratorBlock generator = findByBlock(block);
        if (generator != null) {
            try {
                removeGenerator(generator);
            } catch (Exception e) {
                return;
            }
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
