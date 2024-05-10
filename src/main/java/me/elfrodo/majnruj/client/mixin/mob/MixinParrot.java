package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Parrot.class)
public abstract class MixinParrot extends Mob implements RidableEntity {
    public MixinParrot(EntityType<? extends Parrot> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().parrot.x, getSeats().parrot.y, getSeats().parrot.z);
    }
}
