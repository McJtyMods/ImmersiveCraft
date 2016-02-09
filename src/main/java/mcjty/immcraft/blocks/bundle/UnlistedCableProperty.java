package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.cables.CableSectionRender;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class UnlistedCableProperty implements IUnlistedProperty<List<CableSectionRender>> {

    private final String name;

    public UnlistedCableProperty(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(List<CableSectionRender> value) {
        return true;
    }

    @Override
    public Class<List<CableSectionRender>> getType() {
        return (Class<List<CableSectionRender>>) (Class) List.class;
    }

    @Override
    public String valueToString(List<CableSectionRender> value) {
        return value.toString();
    }
}
