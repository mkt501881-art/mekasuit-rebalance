package com.example.examplemod.mixin;

import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMekaSuitArmor.class)
public class MekaSuitMixin {

    @Inject(
            method = "getDamageAbsorbed",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void testInject(
            Player player,
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Float> cir
    ) {
        String damageType = source.getMsgId();

        if (
                damageType.equals("fall")
                        || damageType.equals("lava")
                        || damageType.equals("inFire")
                        || damageType.equals("onFire")
                        || damageType.equals("hotFloor")
                        || damageType.equals("drown")
                        || damageType.equals("freeze")
                        || damageType.equals("cactus")
                        || damageType.equals("sweetBerryBush")
                        || damageType.equals("starve")
                        || damageType.equals("cramming")
                        || damageType.equals("outOfWorld")
        ) {
            return;
        }

        int level = player.experienceLevel;

        if (level <= 50) {
            return;
        }

        double failChance =
                Math.min(0.5D, (level - 50) / 100.0D);

        if (player.getRandom().nextDouble() < failChance) {

            int chancePercent = (int) (failChance * 100);

            MutableComponent chanceText;

            if (chancePercent >= 50) {
                chanceText = Component.literal(chancePercent + "%")
                        .withStyle(ChatFormatting.RED);
            } else if (chancePercent >= 30) {
                chanceText = Component.literal(chancePercent + "%")
                        .withStyle(ChatFormatting.YELLOW);
            } else {
                chanceText = Component.literal(chancePercent + "%")
                        .withStyle(ChatFormatting.GREEN);
            }

            player.displayClientMessage(
                    Component.literal("貫通!")
                            .withStyle(ChatFormatting.RED)
                            .append(
                                    Component.literal(" 現在の確率: ")
                                            .withStyle(ChatFormatting.WHITE)
                            )
                            .append(chanceText),
                    true
            );

            cir.setReturnValue(0F);
        }
    }
}