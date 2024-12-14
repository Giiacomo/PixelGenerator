package me.giacomo.minecraft.pixelGenerator.db;

import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;

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
                "blockMaterial TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "interval INTEGER NOT NULL, " +
                "PRIMARY KEY (x, y, z, world)" +
                ")");
    }

    public void saveGenerator(int x, int y, int z, String world, String material, String blockMaterial, int quantity, int interval) throws SQLException {
        String sql = "INSERT OR REPLACE INTO generators (x, y, z, world, material, blockMaterial, quantity, interval) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            stmt.setString(4, world);
            stmt.setString(5, material);
            stmt.setString(6, blockMaterial); // Impostiamo il nuovo materiale
            stmt.setInt(7, quantity);
            stmt.setInt(8, interval);
            stmt.executeUpdate();
        }
    }

    public void saveGenerator(AbstractGeneratorBlock generatorBlock) throws SQLException {
        int x = generatorBlock.getBlock().getX();
        int y = generatorBlock.getBlock().getY();
        int z = generatorBlock.getBlock().getZ();
        String world = generatorBlock.getBlock().getWorld().getName();
        String material = generatorBlock.getItemToGenerateName();
        String blockMaterial = generatorBlock.getBlock().getType().name();
        int quantity = generatorBlock.getQuantity();
        int interval = generatorBlock.getInterval();

        saveGenerator(x, y, z, world, material, blockMaterial, quantity, interval);
    }

    public void updateGeneratorPosition(AbstractGeneratorBlock generatorBlock, int newX, int newY, int newZ) throws SQLException {
        String sql = "UPDATE generators SET x = ?, y = ?, z = ? WHERE x = ? AND y = ? AND z = ? AND world = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, newX);
            stmt.setInt(2, newY);
            stmt.setInt(3, newZ);

            stmt.setInt(4, generatorBlock.getBlock().getX());
            stmt.setInt(5, generatorBlock.getBlock().getY());
            stmt.setInt(6, generatorBlock.getBlock().getZ());
            stmt.setString(7, generatorBlock.getBlock().getWorld().getName());

            stmt.executeUpdate();
        }
    }

    public void updateGeneratorParameters(AbstractGeneratorBlock generatorBlock, int interval, int quantity) throws SQLException {
        String sql = "UPDATE generators SET interval = ?, quantity = ? WHERE x = ? AND y = ? AND z = ? AND world = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, interval);
            stmt.setInt(2, quantity);

            stmt.setInt(3, generatorBlock.getBlock().getX());
            stmt.setInt(4, generatorBlock.getBlock().getY());
            stmt.setInt(5, generatorBlock.getBlock().getZ());
            stmt.setString(6, generatorBlock.getBlock().getWorld().getName());

            stmt.executeUpdate();
        }
    }

    public void removeGenerator(AbstractGeneratorBlock generatorBlock) throws SQLException {
        String sql = "DELETE FROM generators WHERE x = ? AND y = ? AND z = ? AND world = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, generatorBlock.getBlock().getX());
            stmt.setInt(2, generatorBlock.getBlock().getY());
            stmt.setInt(3, generatorBlock.getBlock().getZ());
            stmt.setString(4, generatorBlock.getBlock().getWorld().getName());
            stmt.executeUpdate();
        }
    }

    // Metodo aggiornato per caricare anche blockMaterial
    public List<Generator> loadGenerators() throws SQLException {
        String sql = "SELECT x, y, z, world, material, blockMaterial, quantity, interval FROM generators";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            List<Generator> generators = new ArrayList<>();
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String world = rs.getString("world");
                String material = rs.getString("material");
                String blockMaterial = rs.getString("blockMaterial");
                int quantity = rs.getInt("quantity");
                int interval = rs.getInt("interval");
                generators.add(new Generator(x, y, z, world, material, blockMaterial, quantity, interval));
            }
            return generators;
        }
    }

    public static class Generator {
        private final int x, y, z;
        private final String world;
        private final String material;
        private final String blockMaterial;
        private final int quantity, interval;

        public Generator(int x, int y, int z, String world, String material, String blockMaterial, int quantity, int interval) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
            this.material = material;
            this.blockMaterial = blockMaterial;
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

        public String getBlockMaterial() {
            return blockMaterial;
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
