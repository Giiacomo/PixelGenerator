package me.giacomo.minecraft.pixelGenerator.db;

import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;

import javax.swing.plaf.PanelUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratorDB {
    private final Connection con;

    public GeneratorDB(String path) throws SQLException {
        this.con = DriverManager.getConnection("jdbc:sqlite:" + path);
        Statement stmt = con.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS generators (" +
                "x INTEGER NOT NULL, " +
                "y INTEGER NOT NULL, " +
                "z INTEGER NOT NULL, " +
                "world TEXT NOT NULL, " +
                "material TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "interval INTEGER NOT NULL, " +
                "PRIMARY KEY (x, y, z, world)" +
                ")");
    }

    public void saveGenerator(int x, int y, int z, String world, String material, int quantity, int interval) throws SQLException {
        String sql = "INSERT OR REPLACE INTO generators (x, y, z, world, material, quantity, interval) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            stmt.setString(4, world);
            stmt.setString(5, material);
            stmt.setInt(6, quantity);
            stmt.setInt(7, interval);
            stmt.executeUpdate();
        }
    }

    public void saveGenerator(GeneratorBlock generatorBlock) throws SQLException {
        int x = generatorBlock.getBlock().getX();
        int y = generatorBlock.getBlock().getY();
        int z = generatorBlock.getBlock().getZ();
        String world = generatorBlock.getBlock().getWorld().getName();
        String material = generatorBlock.getItemToGenerate().name();
        int quantity = generatorBlock.getQuantity();
        int interval = generatorBlock.getInterval();

        saveGenerator(x, y, z, world, material, quantity, interval);
    }

    public void removeGenerator(GeneratorBlock generatorBlock) throws SQLException {
        String sql = "DELETE FROM generators WHERE x = ? AND y = ? AND z = ? AND world = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, generatorBlock.getBlock().getX());
            stmt.setInt(2, generatorBlock.getBlock().getY());
            stmt.setInt(3, generatorBlock.getBlock().getZ());
            stmt.setString(4, generatorBlock.getBlock().getWorld().getName());
            stmt.executeUpdate();
        }

    }

    // Method to load all generator data
    public List<Generator> loadGenerators() throws SQLException {
        String sql = "SELECT x, y, z, world, material, quantity, interval FROM generators";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            List<Generator> generators = new ArrayList<>();
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String world = rs.getString("world");
                String material = rs.getString("material");
                int quantity = rs.getInt("quantity");
                int interval = rs.getInt("interval");
                generators.add(new Generator(x, y, z, world, material, quantity, interval));
            }
            return generators;
        }
    }

    public static class Generator {
        private final int x, y, z;
        private final String world;
        private final String material;
        private final int quantity, interval;

        public Generator(int x, int y, int z, String world, String material, int quantity, int interval) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
            this.material = material;
            this.quantity = quantity;
            this.interval = interval;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public String getMaterial() {
            return material;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getInterval() {
            return interval;
        }

        public String getWorld() {
            return world;
        }
    }
}
