package me.elfrodo.majnruj.client.mixin.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import me.elfrodo.majnruj.client.entity.RidableEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Villager.class)
public abstract class MixinVillager extends Mob implements RidableEntity {
    public MixinVillager(EntityType<? extends Villager> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return super.getPassengerRidingPosition(passenger).add(getSeats().villager.x, getSeats().villager.y, getSeats().villager.z);
    }
}
