package xyz.jpenilla.squaremap.fabric.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.jpenilla.squaremap.fabric.event.ServerPlayerEvents;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin {
    @Inject(
        method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V",
        at = @At("TAIL")
    )
    void injectTeleportTo(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        ServerPlayerEvents.WORLD_CHANGED.invoker().worldChanged((ServerPlayer) (Object) this);
    }

    @Inject(
        method = "changeDimension",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;sendLevelInfo(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerLevel;)V",
            shift = At.Shift.AFTER
        )
    )
    void injectChangeDimension(DimensionTransition dimensionTransition, CallbackInfoReturnable<Entity> cir) {
        ServerPlayerEvents.WORLD_CHANGED.invoker().worldChanged((ServerPlayer) (Object) this);
    }
}
