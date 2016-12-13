package mcjty.immcraft.blocks.foliage;

import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRock extends EntityThrowable {

    public EntityRock(World worldIn) {
        super(worldIn);
    }

    public EntityRock(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityRock(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), GeneralConfiguration.rockDamage);
        }

        if (!this.getEntityWorld().isRemote) {
            this.setDead();
        }
    }
}