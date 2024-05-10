package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MushroomCow.class)
public abstract class MixinMooshroom extends Mob implements RidableEntity {
    public MixinMooshroom(EntityType<? extends MushroomCow> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().mooshroom.x, getSeats().mooshroom.y, getSeats().mooshroom.z);
    }
}
