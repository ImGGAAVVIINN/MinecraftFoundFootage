package com.sp.sounds.entity;

import com.sp.entity.custom.BacteriaEntity;
import com.sp.init.ModSounds;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

public class BacteriaChaseSoundInstance extends MovingSoundInstance {
    private final BacteriaEntity entity;
    private int fadeOutTicks = 0;
    private boolean shouldStop = false;

    public BacteriaChaseSoundInstance(BacteriaEntity entity) {
        super(ModSounds.BACTERIA_CHASE, SoundCategory.HOSTILE, SoundInstance.createRandom());
        this.entity = entity;
        this.setPosition(entity);
        this.repeat = false; // Don't repeat - play once to completion
        this.repeatDelay = 0;
        this.volume = 1.0f;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return false; // Don't loop the same sound
    }

    @Override
    public void tick() {
        if(this.entity.isRemoved() || this.entity.getTarget() == null || shouldStop){
            // Fade out when entity is removed or no longer has a target
            this.fadeOutTicks++;
            this.volume = Math.max(0, 1.0f - (this.fadeOutTicks / 40.0f));
            
            if(this.fadeOutTicks >= 40) {
                this.setDone();
            }
        } else {
            // Reset fade if entity starts chasing again
            this.fadeOutTicks = 0;
            this.volume = 1.0f;
        }

        this.setPosition(this.entity);
    }

    public void markForStop() {
        this.shouldStop = true;
    }

    private void setPosition(BacteriaEntity entity){
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }
}
