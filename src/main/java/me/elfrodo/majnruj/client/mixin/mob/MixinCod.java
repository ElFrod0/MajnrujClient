package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Cod.class)
public abstract class MixinCod extends Mob implements RidableEntity {
    public MixinCod(EntityType<? extends Cod> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().cod.x, getSeats().cod.y, getSeats().cod.z);
    }
}
