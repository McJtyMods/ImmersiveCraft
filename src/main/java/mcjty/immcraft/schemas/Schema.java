package mcjty.immcraft.schemas;

import mcjty.lib.tools.InventoryTools;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Schema {
    private final String name;
    private final ItemStack result;
    private final List<ItemStack> inputs = new ArrayList<>();

    public Schema(String name, ItemStack result, ItemStack... in) {
        this.name = name;
        this.result = result;
        Collections.addAll(inputs, in);
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult() {
        return result;
    }

    public boolean match(Collection<ItemStack> in, EntityPlayer player) {
        List<ItemStack> materials = Stream.concat(in.stream(),
                hotbarStream(player))
                .filter(ItemStackTools::isValid)
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return !inputs.stream().anyMatch(input -> ItemStackTools.isValid(input) && !consume(input, null, materials));
    }

    private Stream<ItemStack> hotbarStream(EntityPlayer player) {
        return InventoryTools.getMainInventory(player).stream().limit(9);
    }

    public List<ItemStack> getMissing(List<ItemStack> in, EntityPlayer player) {
        List<ItemStack> materials = Stream.concat(in.stream(), hotbarStream(player))
                .filter(ItemStackTools::isValid)
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return inputs.stream()
                .filter(input -> ItemStackTools.isValid(input) && !consume(input, null, materials))
                .collect(Collectors.toList());
    }

    public List<ItemStack> getPresent(List<ItemStack> in, EntityPlayer player) {
        List<ItemStack> materials = Stream.concat(in.stream(), hotbarStream(player))
                .filter(ItemStackTools::isValid)
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return inputs.stream()
                .filter(input -> ItemStackTools.isValid(input) && consume(input, null, materials))
                .collect(Collectors.toList());
    }

    private boolean consume(ItemStack input, EntityPlayer player, List<ItemStack> materials) {
        Integer[] a = new Integer[] { ItemStackTools.getStackSize(input) };
        materials.stream().filter(input::isItemEqual).forEach(s -> consumeStack(a, s));
        if (player != null && a[0] > 0) {
            hotbarStream(player).filter(input::isItemEqual).forEach(s -> consumeStack(a, s));
        }
        return a[0] == 0;
    }

    private void consumeStack(Integer[] amount, ItemStack stack) {
        int d = Math.min(amount[0], ItemStackTools.getStackSize(stack));
        amount[0] -= d;
        ItemStackTools.incStackSize(stack, -d);
    }

    // Call this only when match() returns true.
    public ItemStack craft(List<ItemStack> in, EntityPlayer player) {
        if (inputs.stream().anyMatch(input -> ItemStackTools.isValid(input) && !consume(input, player, in))) {
            return ItemStackTools.getEmptyStack();
        }
        // Remove the itemstacks where nothing is left.
        IntStream.range(0, in.size())
                .filter(i -> ItemStackTools.isValid(in.get(i)) && ItemStackTools.getStackSize(in.get(i)) == 0)
                .forEach(i -> in.set(i, ItemStackTools.getEmptyStack()));

        List<ItemStack> inv = InventoryTools.getMainInventory(player);
        IntStream.range(0, 9)
                .filter(i -> ItemStackTools.isValid(inv.get(i)) && ItemStackTools.getStackSize(inv.get(i)) == 0)
                .forEach(i -> inv.set(i, ItemStackTools.getEmptyStack()));

        return result.copy();
    }
}
