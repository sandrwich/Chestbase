package pl.daffit.chestbase;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.daffit.chestbase.structure.DataBlock;
import pl.daffit.chestbase.structure.DataPos;
import pl.daffit.chestbase.validation.DataValidator;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

public class Chestbase {

    private DataValidator dataValidator;
    private World world;

    public Chestbase(String worldName) {
        this.world = Bukkit.getWorld(worldName);
        this.dataValidator = new DataValidator();
    }

    public World getWorld() {
        return this.world;
    }

    public DataValidator getDataValidator() {
        return this.dataValidator;
    }

    public static BitSet longToBitset(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if ((value % 2L) != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static long bitsetToLong(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }

    public static void test(int x, int y, int z, int... positive) throws IOException {

        Chestbase chestbase = new Chestbase("world");
        DataBlock dataCell = new DataBlock(chestbase, new DataPos(x, y, z));

        BitSet data = new BitSet();
        Arrays.stream(positive).forEach(data::set);

        Bukkit.broadcastMessage("writing " + data);
        dataCell.write(data);

        BitSet read = dataCell.read();
        Bukkit.broadcastMessage(read.toString());
    }

    public static void testChunk(Player player) throws IOException {

        Chunk chunk = player.getLocation().getChunk();
        long startTime = System.currentTimeMillis();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {

                    Block block = chunk.getBlock(x, y, z);
                    block.setType(Material.AIR);

                    write(block.getX(), block.getY(), block.getZ(),
                            1, 2, 3, 4, 5, 6, 7, 12, 13, 14, 15, 65, 70, 90, 120, 150, 151, 152, 153, 154);
                }
            }
        }

        long took = System.currentTimeMillis() - startTime;
        Bukkit.broadcastMessage("Chunk filling took " + took + " ms!");
    }

    public static void write(int x, int y, int z, int... positive) throws IOException {

        Chestbase chestbase = new Chestbase("world");
        DataBlock dataCell = new DataBlock(chestbase, new DataPos(x, y, z));

        BitSet data = new BitSet();
        Arrays.stream(positive).forEach(data::set);

        Bukkit.broadcastMessage("Writing " + dataCell.getPos() + ": " + data);
        long startTime = System.nanoTime();
        dataCell.write(data);
        long took = System.nanoTime() - startTime;
        Bukkit.broadcastMessage("Took " + took + " ns!");
    }

    public static void read(int x, int y, int z) throws IOException {

        Chestbase chestbase = new Chestbase("world");
        DataBlock dataCell = new DataBlock(chestbase, new DataPos(x, y, z));

        Bukkit.broadcastMessage("Reading " + dataCell.getPos());
        long startTime = System.nanoTime();
        BitSet read = dataCell.read();
        long took = System.nanoTime() - startTime;
        Bukkit.broadcastMessage("Took " + took + " ns: " + read);
    }
}
