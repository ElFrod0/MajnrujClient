package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import me.elfrodo.majnruj.client.mixin.accessor.AccessAbstractPiglin;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Piglin.class)
public abstract class MixinPiglin extends Mob implements RidableEntity {
    public MixinPiglin(EntityType<? extends Piglin> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().piglin.x, getSeats().piglin.y, getSeats().piglin.z);
    }

    @Override
    public boolean isNoAi() {
        // silly hack to stop piglin from shaking on preview screen
        return ((AccessAbstractPiglin) this).getTimeInOverworld() < 0 || super.isNoAi();
    }
}
