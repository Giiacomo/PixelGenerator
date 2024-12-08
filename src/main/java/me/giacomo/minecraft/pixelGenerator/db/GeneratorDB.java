package me.giacomo.minecraft.pixelGenerator.db;

import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;

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
                "material TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "interval INTEGER NOT NULL, " +
                "PRIMARY KEY (x, y, z)" +
                ")");
    }

    public void saveGenerator(int x, int y, int z, String material, int quantity, int interval) throws SQLException {
        String sql = "INSERT OR REPLACE INTO generators (x, y, z, material, quantity, interval) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            stmt.setString(4, material);
            stmt.setInt(5, quantity);
            stmt.setInt(6, interval);
            stmt.executeUpdate();
        }
    }

    public void saveGenerator(GeneratorBlock generatorBlock) throws SQLException {
        int x = generatorBlock.getBlock().getX();
        int y = generatorBlock.getBlock().getY();
        int z = generatorBlock.getBlock().getZ();
        String material = generatorBlock.getItemToGenerate().name();
        int quantity = generatorBlock.getQuantity();
        int interval = generatorBlock.getInterval();

        saveGenerator(x, y, z, material, quantity, interval);

    }

    // Method to load all generator data
    public List<Generator> loadGenerators() throws SQLException {
        String sql = "SELECT x, y, z, material, quantity, interval FROM generators";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            List<Generator> generators = new ArrayList<>();
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String material = rs.getString("material");
                int quantity = rs.getInt("quantity");
                int interval = rs.getInt("interval");
                generators.add(new Generator(x, y, z, material, quantity, interval));
            }
            return generators;
        }
    }

    public static class Generator {
        private final int x, y, z;
        private final String material;
        private final int quantity, interval;

        public Generator(int x, int y, int z, String material, int quantity, int interval) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.material = material;
            this.quantity = quantity;
            this.interval = interval;
        }

        // Getters for the generator fields
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

        @Override
        public String toString() {
            return "Generator{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", material='" + material + '\'' +
                    ", quantity=" + quantity +
                    ", interval=" + interval +
                    '}';
        }
    }
}
