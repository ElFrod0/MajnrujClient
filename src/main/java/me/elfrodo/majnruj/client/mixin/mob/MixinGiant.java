package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Giant.class)
public abstract class MixinGiant extends Mob implements RidableEntity {
    public MixinGiant(EntityType<? extends Giant> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().giant.x, getSeats().giant.y, getSeats().giant.z);
    }
}
