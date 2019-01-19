package pl.daffit.chestbase.validation;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import pl.daffit.chestbase.structure.DataPos;

public class DataValidator {

    public boolean isValidType(DataPos pos, Block block) {
        return pos.toType() == block.getType();
    }

    public boolean isWritable(DataPos pos, World world) {
        Block block = pos.toBlock(world);
        return (block.getType() == Material.AIR)
                || (block.getType() == Material.CHEST)
                || (block.getType() == Material.TRAPPED_CHEST);
    }

    public boolean isValidBit(ItemStack stack) {
        return (stack == null) || (stack.getType() == Material.STONE);
    }
}
