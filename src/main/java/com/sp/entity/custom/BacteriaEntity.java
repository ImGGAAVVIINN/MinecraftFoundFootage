package com.sp.entity.custom;

import com.sp.clientWrapper.ClientWrapper;
import com.sp.sounds.entity.BacteriaChaseSoundInstance;
import com.sp.init.BackroomsLevels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A simple hostile "bacteria" entity that behaves similarly to a zombie but deals much higher damage.
 */
public class BacteriaEntity extends HostileEntity {

    public BacteriaChaseSoundInstance chaseSoundInstance;
    private Entity prevTarget;
    private boolean isChasing = false;

    public BacteriaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // Basic hostile behaviour: swim, melee attack, wander, look at player, idle look around
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));

        // Target players
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // Default handling; could add special resistances later
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        // Handle chase sound - server sends status to client
        if (!this.getWorld().isClient) {
            LivingEntity target = this.getTarget();
            if (target != null && !isChasing) {
                // Started chasing - notify client to play chase sound
                this.getWorld().sendEntityStatus(this, (byte) 124);
                isChasing = true;
                prevTarget = target;
            } else if (target == null && isChasing) {
                // Stopped chasing - notify client to stop
                this.getWorld().sendEntityStatus(this, (byte) 125);
                isChasing = false;
                prevTarget = null;
            }
        } else {
            // Client-side: check if current sound finished and still chasing
            if (isChasing && chaseSoundInstance != null && chaseSoundInstance.isDone()) {
                // Sound finished, play another one
                ClientWrapper.handleBacteriaEntityClientSide(this);
            }
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == (byte) 124 && this.getWorld().isClient) {
            // Start chasing
            isChasing = true;
            ClientWrapper.handleBacteriaEntityClientSide(this);
        } else if (status == (byte) 125 && this.getWorld().isClient) {
            // Stop chasing
            isChasing = false;
            if (chaseSoundInstance != null) {
                chaseSoundInstance.markForStop();
            }
        }
        super.handleStatus(status);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.getWorld().isClient) {
            ClientWrapper.onRemoveBacteriaClientSide(this);
        }
        super.remove(reason);
    }

    @Nullable
    public static DefaultAttributeContainer.Builder createBacteriaAttributes() {
        // High-damage hostile: increased health and very high attack damage
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 64.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 4.0D);
    }

    /**
     * Custom spawn restriction: only spawn in Level 0, below y=25, with 0.01% chance
     */
    public static boolean canSpawn(EntityType<BacteriaEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        // Restrict to Level 0 dimension only
        boolean isLevel0 = world.toServerWorld().getRegistryKey().equals(BackroomsLevels.LEVEL0_WORLD_KEY);
        // Only 0.01% chance to spawn (1 in 10,000 - extremely rare)
        boolean randomChance = random.nextFloat() < 0.0001f;
        return isLevel0 && pos.getY() < 25 && randomChance && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
    }
}
