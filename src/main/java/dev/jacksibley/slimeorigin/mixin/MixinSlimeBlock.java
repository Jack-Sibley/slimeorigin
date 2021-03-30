package dev.jacksibley.slimeorigin.mixin;

import dev.jacksibley.slimeorigin.Slimeorigin;
import net.minecraft.block.Block;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class MixinSlimeBlock {
    @Inject(method = "bounce", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V", ordinal = 0), cancellable = true)
    public void bounce(Entity entity, CallbackInfo ci)  {
        if (Slimeorigin.BOUNCY.isActive(entity) && !entity.bypassesLandingEffects()) {
            Vec3d vec3d = entity.getVelocity();
            entity.setVelocity(vec3d.x, -vec3d.y * 1.6, vec3d.z);
            BlockSoundGroup blockSoundGroup = BlockSoundGroup.SLIME;

            entity.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.1F, blockSoundGroup.getPitch());

            ci.cancel();
        }

    }

}