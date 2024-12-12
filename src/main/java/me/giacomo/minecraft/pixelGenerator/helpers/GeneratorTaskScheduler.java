package me.giacomo.minecraft.pixelGenerator.helpers;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class TaskScheduler {
    private final Runnable task;
    private final long delay;
    private final long interval;

    public TaskScheduler(Runnable task, long delay, long interval) {
        this.task = task;
        this.delay = delay;
        this.interval = interval;
    }

    public BukkitTask schedule() {
        Bukkit.getLogger().info("Scheduling task with delay " + delay + " and interval " + interval);

        return Bukkit.getScheduler().runTaskTimer(
                PixelGenerator.getInstance(),
                task,
                delay,
                interval
        );
    }
}
