package mcjty.immcraft.blocks.foliage;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyEnum;

import java.util.Collection;

public class PropertyAmount extends PropertyEnum<EnumAmount> {
    protected PropertyAmount(String name, Collection<EnumAmount> values) {
        super(name, EnumAmount.class, values);
    }

    public static PropertyAmount create(String name) {
        return create(name, Predicates.<EnumAmount>alwaysTrue());
    }

    public static PropertyAmount create(String name, Predicate<EnumAmount> filter) {
        return create(name, Collections2.filter(Lists.newArrayList(EnumAmount.values()), filter));
    }

    public static PropertyAmount create(String name, Collection<EnumAmount> values) {
        return new PropertyAmount(name, values);
    }
}