package mcjty.immcraft.blocks.book;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import mcjty.immcraft.blocks.foliage.EnumAmount;
import net.minecraft.block.properties.PropertyEnum;

import java.util.Collection;

public class PropertyStandState extends PropertyEnum<EnumStandState> {
    protected PropertyStandState(String name, Collection<EnumStandState> values) {
        super(name, EnumStandState.class, values);
    }

    public static PropertyStandState create(String name) {
        return create(name, Predicates.alwaysTrue());
    }

    public static PropertyStandState create(String name, Predicate<EnumStandState> filter) {
        return create(name, Collections2.filter(Lists.newArrayList(EnumStandState.values()), filter));
    }

    public static PropertyStandState create(String name, Collection<EnumStandState> values) {
        return new PropertyStandState(name, values);
    }
}