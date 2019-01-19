package pl.daffit.chestbase.structure;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.daffit.chestbase.Chestbase;
import pl.daffit.chestbase.validation.DataValidationException;
import pl.daffit.chestbase.validation.DataValidator;

import java.io.IOException;
import java.util.BitSet;

public class DataBlock {

    public static final int SECTOR_SIZE = 27;

    private final DataPos pos;
    private final World world;
    private final DataValidator dataValidator;

    public DataBlock(Chestbase chestbase, DataPos pos) {
        this.pos = pos;
        this.world = chestbase.getWorld();
        this.dataValidator = chestbase.getDataValidator();
    }

    public DataPos getPos() {
        return this.pos;
    }

    public BitSet read() throws IOException {

        Block block = this.pos.toBlock(this.world);
        if (!this.dataValidator.isValidType(this.pos, block)) {
            throw new DataValidationException("Invalid blocktype found. Block is broken.");
        }

        BlockState blockState = block.getState();
        Chest chest = (Chest) blockState;

        Inventory inventory = chest.getBlockInventory();
        ItemStack[] contents = inventory.getContents();

        BitSet bits = new BitSet();
        int bitIndex = 0;

        for (int i = 0; i < SECTOR_SIZE; i++) {

            if (!this.dataValidator.isValidBit(contents[i])) {
                String foundType = contents[i].getType().name();
                throw new DataValidationException("Corrupted cell in block " + this.pos + ", index: " + i + "! " +
                        "Expected " + Material.STONE.name() + " but found " + foundType + ".");
            }

            DataCell cell = new DataCell(contents[i]);
            BitSet cellBits = cell.read();

            for (int j = 0; j < DataCell.CELL_BITS; j++) {
                bits.set(bitIndex, cellBits.get(j));
                bitIndex++;
            }
        }

        return bits;
    }

    public void write(BitSet data) throws IOException {

        if (!this.dataValidator.isWritable(this.pos, this.world)) {
            throw new DataValidationException(this.pos + " is not writable.");
        }

        int bitsCount = data.size();
        int lastBitIndex = 0;
        long maxData = (SECTOR_SIZE * DataCell.CELL_BITS) - 1;

        for (int i = bitsCount - 1; i >= 0; i--) {
            if (!data.get(i)) {
                continue;
            }
            lastBitIndex = i;
            break;
        }

        if (lastBitIndex > maxData) {
            throw new DataValidationException(this.pos + " overflow occured: " + lastBitIndex + " > " + maxData);
        }

        Block block = this.pos.toBlock(this.world);
        block.setType(this.pos.toType());

        BlockState blockState = block.getState();
        Chest chest = (Chest) blockState;

        Inventory inventory = chest.getBlockInventory();
        ItemStack[] contents = inventory.getContents();

        int bitIndex = 0;
        for (int i = 0; i < SECTOR_SIZE; i++) {

            ItemStack stack = new ItemStack(Material.STONE);
            BitSet cellBits = new BitSet();

            for (int j = 0; j < DataCell.CELL_BITS; j++) { // bierzemy kolejne 6 bitow
                cellBits.set(j, data.get(bitIndex));
                bitIndex++;
            }

            stack.setAmount((int) Chestbase.bitsetToLong(cellBits));
            contents[i] = stack;
        }

        inventory.setContents(contents);
    }
}
