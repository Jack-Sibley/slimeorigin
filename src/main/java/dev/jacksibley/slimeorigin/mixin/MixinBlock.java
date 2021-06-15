package dev.jacksibley.slimeorigin.mixin;

import dev.jacksibley.slimeorigin.Slimeorigin;
import io.github.apace100.apoli.power.PowerTypeReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
//import virtuoel.pehkui.api.ScaleType;
//import virtuoel.pehkui.util.ScaleUtils;

import java.util.Random;

import static dev.jacksibley.slimeorigin.Slimeorigin.SLIME_SIZE;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "onEntityLand", at = @At("HEAD"),cancellable = true)
    public void onEntityLand(BlockView world, Entity entity, CallbackInfo ci) {
        if (Slimeorigin.BOUNCY.isActive(entity) && !entity.bypassesLandingEffects()) {
                Vec3d vec3d = entity.getVelocity();
                if (vec3d.y < -0.1) {
                    double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
                    entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
                    BlockSoundGroup blockSoundGroup = BlockSoundGroup.SLIME;

                    entity.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.1F, blockSoundGroup.getPitch());
                } else {
                    entity.setVelocity(entity.getVelocity().multiply(1.0D, 0.0D, 1.0D));
                }
                ci.cancel();

                return;
        }
    }

}