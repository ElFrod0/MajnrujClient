package org.purpurmc.purpur.client.mixin.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import org.purpurmc.purpur.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WolfEntity.class)
public abstract class MixinWolf extends MobEntity implements RidableEntity {
    public MixinWolf(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public double getMountedHeightOffset() {
        return (double) getHeight() * getSeats().wolf.y;
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        updatePassengerPosition(passenger, getSeats().wolf);
    }
}