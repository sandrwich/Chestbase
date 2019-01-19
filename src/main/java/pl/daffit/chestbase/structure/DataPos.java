package pl.daffit.chestbase.structure;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.StringJoiner;

public class DataPos implements Addressable {

    private final int x;
    private final int y;
    private final int z;

    public DataPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    public Block toBlock(World world) {
        return world.getBlockAt(this.x, this.y, this.z);
    }

    public Material toType() {

        boolean xParity = (this.x % 2) == 0;
        boolean zParity = (this.z % 2) == 0;

        if (xParity) {
            if (zParity) {
                return Material.CHEST;
            } else {
                return Material.TRAPPED_CHEST;
            }
        } else {
            if (zParity) {
                return Material.TRAPPED_CHEST;
            } else {
                return Material.CHEST;
            }
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataPos.class.getSimpleName() + "[", "]")
                .add("x=" + this.x)
                .add("y=" + this.y)
                .add("z=" + this.z)
                .toString();
    }
}
