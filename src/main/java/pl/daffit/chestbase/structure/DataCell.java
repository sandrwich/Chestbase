package pl.daffit.chestbase.structure;

import org.bukkit.inventory.ItemStack;
import pl.daffit.chestbase.Chestbase;
import pl.daffit.chestbase.validation.DataValidationException;

import java.io.IOException;
import java.util.BitSet;

public class DataCell {

    public static final int BLOCK_SIZE = 0b111111;
    public static final int CELL_BITS = 6;

    private final ItemStack stack;

    public DataCell(ItemStack stack) {
        this.stack = stack;
    }

    BitSet read() throws IOException {

        if (this.stack == null) {
            return new BitSet();
        }

        int amount = this.stack.getAmount();
        if (amount > BLOCK_SIZE) {
            throw new DataValidationException("Cell is overloaded: " + amount + " > " + BLOCK_SIZE + "!");
        }

        return Chestbase.longToBitset(amount);
    }
}
