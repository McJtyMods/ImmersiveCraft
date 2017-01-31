package mcjty.immcraft.sound;

import net.minecraft.block.Block;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ImmersiveSound extends MovingSound {

    private final float baseVolume;

    public ImmersiveSound(SoundEvent event, World world, BlockPos pos, float baseVolume, boolean repeat) {
        super(event, SoundCategory.BLOCKS);
        this.world = world;
        this.pos = pos;
        this.xPosF = pos.getX();
        this.yPosF = pos.getY();
        this.zPosF = pos.getZ();
        this.attenuationType = AttenuationType.LINEAR;
        this.repeat = repeat;
        this.repeatDelay = 0;
        this.sound = event;
        this.baseVolume = baseVolume;
    }

    private final World world;
    private final BlockPos pos;
    private final SoundEvent sound;
    private float vol = 0.0f;

    @Override
    public void update() {
        Block block = world.getBlockState(pos).getBlock();
        if (!(block instanceof ISoundProducer)) {
            donePlaying = true;
            return;
        }
        volume = baseVolume * vol;
    }

    public void setVolume(float volume) {
        this.vol = volume;
    }

    protected boolean isSoundType(SoundEvent event){
        return sound == event;
    }

}
