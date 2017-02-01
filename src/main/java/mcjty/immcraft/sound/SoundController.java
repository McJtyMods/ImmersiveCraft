package mcjty.immcraft.sound;

import com.google.common.collect.Maps;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@SideOnly(Side.CLIENT)
public final class SoundController {

    private static SoundEvent pageturn;

    public static void init() {
        pageturn = registerSound(new ResourceLocation(ImmersiveCraft.MODID, "pageturn"));
    }

    private static final Map<Pair<Integer, BlockPos>, ImmersiveSound> sounds = Maps.newHashMap();

    private static SoundEvent registerSound(ResourceLocation rl){
        SoundEvent ret = new SoundEvent(rl).setRegistryName(rl);
        ((FMLControlledNamespacedRegistry) SoundEvent.REGISTRY).register(ret);
        return ret;
    }

    public static void stopSound(World worldObj, BlockPos pos) {
        Pair<Integer, BlockPos> g = fromPosition(worldObj, pos);
        if (sounds.containsKey(g)) {
            MovingSound movingSound = sounds.get(g);
            Minecraft.getMinecraft().getSoundHandler().stopSound(movingSound);
            sounds.remove(g);
        }
    }

    private static void playSound(World worldObj, BlockPos pos, SoundEvent soundType, float volume, float baseVolume, boolean repeat) {
        ImmersiveSound sound = new ImmersiveSound(soundType, worldObj, pos, baseVolume, repeat);
        sound.setVolume(volume);
        stopSound(worldObj, pos);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        Pair<Integer, BlockPos> g = Pair.of(worldObj.provider.getDimension(), pos);
        sounds.put(g, sound);
    }


    public static void playPageturn(World worldObj, BlockPos pos, float volume) {
//        playSound(worldObj, pos, pageturn, volume, GeneralConfiguration.basePageTurnVolume, false);
        worldObj.playSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), pageturn, SoundCategory.BLOCKS, volume, 1.0f, false);
    }


    public static void updateVolume(World worldObj, BlockPos pos, float volume) {
        ImmersiveSound sound = getSoundAt(worldObj, pos);
        if (sound != null) {
            sound.setVolume(volume);
        }
    }

    public static boolean isPageturnPlaying(World worldObj, BlockPos pos) {
        return isSoundTypePlayingAt(pageturn, worldObj, pos);
    }

    private static boolean isSoundTypePlayingAt(SoundEvent event, World world, BlockPos pos){
        ImmersiveSound s = getSoundAt(world, pos);
        return s != null && s.isSoundType(event);
    }

    private static ImmersiveSound getSoundAt(World world, BlockPos pos){
        return sounds.get(fromPosition(world, pos));
    }

    private static Pair<Integer, BlockPos> fromPosition(World world, BlockPos pos){
        return Pair.of(world.provider.getDimension(), pos);
    }

}
