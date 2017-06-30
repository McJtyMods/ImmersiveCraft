package mcjty.immcraft.schemas;

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
                .filter((stack) -> !stack.isEmpty())
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return !inputs.stream().anyMatch(input -> !input.isEmpty() && !consume(input, null, materials));
    }

    private Stream<ItemStack> hotbarStream(EntityPlayer player) {
        return Collections.unmodifiableList(player.inventory.mainInventory).stream().limit(9);
    }

    public List<ItemStack> getMissing(List<ItemStack> in, EntityPlayer player) {
        List<ItemStack> materials = Stream.concat(in.stream(), hotbarStream(player))
                .filter((stack) -> !stack.isEmpty())
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return inputs.stream()
                .filter(input -> !input.isEmpty() && !consume(input, null, materials))
                .collect(Collectors.toList());
    }

    public List<ItemStack> getPresent(List<ItemStack> in, EntityPlayer player) {
        List<ItemStack> materials = Stream.concat(in.stream(), hotbarStream(player))
                .filter((stack) -> !stack.isEmpty())
                .map(ItemStack::copy)
                .collect(Collectors.toList());
        // Do not pass inventoryPlayer here since we already merged those stacks with materials and don't want to modify the inventory of the player
        return inputs.stream()
                .filter(input -> !input.isEmpty() && consume(input, null, materials))
                .collect(Collectors.toList());
    }

    private boolean consume(ItemStack input, EntityPlayer player, List<ItemStack> materials) {
        Integer[] a = new Integer[] {input.getCount()};
        materials.stream().filter(input::isItemEqual).forEach(s -> consumeStack(a, s));
        if (player != null && a[0] > 0) {
            hotbarStream(player).filter(input::isItemEqual).forEach(s -> consumeStack(a, s));
        }
        return a[0] == 0;
    }

    private void consumeStack(Integer[] amount, ItemStack stack) {
        int d = Math.min(amount[0], stack.getCount());
        amount[0] -= d;
        stack.shrink(d);
    }

    // Call this only when match() returns true.
    public ItemStack craft(List<ItemStack> in, EntityPlayer player) {
        if (inputs.stream().anyMatch(input -> !input.isEmpty() && !consume(input, player, in))) {
            return ItemStack.EMPTY;
        }
        // Remove the itemstacks where nothing is left.
        IntStream.range(0, in.size())
                .filter(i -> !in.get(i).isEmpty() && in.get(i).getCount() == 0)
                .forEach(i -> in.set(i, ItemStack.EMPTY));

        List<ItemStack> inv = Collections.unmodifiableList(player.inventory.mainInventory);
        IntStream.range(0, 9)
                .filter(i -> !inv.get(i).isEmpty() && inv.get(i).getCount() == 0)
                .forEach(i -> inv.set(i, ItemStack.EMPTY));

        return result.copy();
    }
}
