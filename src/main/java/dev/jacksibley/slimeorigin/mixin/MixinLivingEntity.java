package dev.jacksibley.slimeorigin.mixin;

import dev.jacksibley.slimeorigin.Slimeorigin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sun.tools.jstat.Scale;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleType;
//import virtuoel.pehkui.api.ScaleData;
//import virtuoel.pehkui.api.ScaleType;
//import virtuoel.pehkui.entity.ResizableEntity;
//import virtuoel.pehkui.util.ScaleUtils;

import java.util.Optional;

import static dev.jacksibley.slimeorigin.Slimeorigin.FRAGMENTATION;
import static dev.jacksibley.slimeorigin.Slimeorigin.SLIME_SIZE;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", require = 1, allow = 1, at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        final DefaultAttributeContainer.Builder builder = info.getReturnValue();
        builder.add(SLIME_SIZE);
    }

    private void revertScale()
    {
        PehkuiConfig pehconf = new PehkuiConfig();
        if ((Boolean)Optional.ofNullable((Boolean)PehkuiConfig.COMMON.keepAllScalesOnRespawn.get()).orElse(false)) {
                int flooredSlime = (int) floor(this.getAttributeInstance(SLIME_SIZE).getValue());
                if (flooredSlime != 0)
                {
                    ScaleType.HEIGHT.getScaleData(this).setScale(ScaleType.HEIGHT.getScaleData(this).getScale() * 2 * (3 - flooredSlime));
                    ScaleType.WIDTH.getScaleData(this).setScale(ScaleType.WIDTH.getScaleData(this).getScale() * 2 * (3 - flooredSlime));
                }
        }
    }

    @Inject(method = "tryUseTotem", at=@At("HEAD"), cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOutOfWorld()) {

            if (Slimeorigin.FRAGMENTATION.isActive(this)) {
                revertScale();
            }

            cir.setReturnValue(false);
        }
        else {
            if (Slimeorigin.FRAGMENTATION.isActive(this)) {
                int flooredSlime = (int) floor(this.getAttributeInstance(SLIME_SIZE).getValue());
                if (flooredSlime == 0)
                {
                    flooredSlime = (int) floor(this.getAttributeInstance(SLIME_SIZE).getValue());
                }

                if (flooredSlime > 1) {
                    EntityAttributeModifier SizeModifier = new EntityAttributeModifier(
                            String.format("FragmentationSize%d",flooredSlime),
                            -1,
                            EntityAttributeModifier.Operation.ADDITION
                    );

                    this.getAttributeInstance(SLIME_SIZE).addPersistentModifier(SizeModifier);

                    EntityAttributeModifier HealthModifier = new EntityAttributeModifier(
                            String.format("FragmentationHealthDummy%d",flooredSlime),
                            -pow(2, flooredSlime),
                            EntityAttributeModifier.Operation.ADDITION
                    );



                    this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(HealthModifier);
                    this.setHealth(1.0F);
                    this.clearStatusEffects();
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 5));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));

                    ScaleType.HEIGHT.getScaleData(this).setScale(ScaleType.HEIGHT.getScaleData(this).getScale()/2);
                    ScaleType.WIDTH.getScaleData(this).setScale(ScaleType.WIDTH.getScaleData(this).getScale()/2);

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                else {
                    this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                            .getModifiers()
                            .stream()
                            .filter(x -> x.getName().contains("FragmentationHealthDummy"))
                            .forEach(x ->
                                    this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                                    .tryRemoveModifier(x.getId())
                            );

                    this.getAttributeInstance(SLIME_SIZE)
                            .getModifiers()
                            .stream()
                            .filter(x -> x.getName().contains("FragmentationSize"))
                            .forEach(x ->
                                    this.getAttributeInstance(SLIME_SIZE)
                                            .tryRemoveModifier(x.getId())
                            );

                    revertScale();

                }
            }
        }
    }


}
