package ultimate.common.mixin;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import ultimate.common.util.UltimateUtil;

@Mixin({ Chunk.class })
public class MixinChunk {
    @Inject(method = "getEntitiesWithinAABBForEntity", at = @At("TAIL"))
    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill,
            Predicate<? super Entity> filter, CallbackInfo info) {
        listToFill.removeIf(UltimateUtil::isUltimatePlayer);
    }

    @Inject(method = "getEntitiesOfTypeWithinAABB", at = @At("TAIL"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class<? extends T> entityClass, AxisAlignedBB aabb,
            List<T> listToFill, Predicate<? super T> filter, CallbackInfo info) {
        listToFill.removeIf(UltimateUtil::isUltimatePlayer);
    }

    @Inject(method = "removeEntity", at = @At("TAIL"), cancellable = true)
    public void removeEntity(Entity entityIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            info.cancel();
        }
    }
}
