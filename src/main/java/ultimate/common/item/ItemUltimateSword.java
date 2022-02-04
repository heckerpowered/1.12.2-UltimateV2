package ultimate.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import ultimate.common.util.UltimateUtil;
import ultimate.common.util.text.LudicrousText;

public final class ItemUltimateSword extends ItemSword {

    public ItemUltimateSword() {
        super(EnumHelper.addToolMaterial("ultimate", Integer.MAX_VALUE, Integer.MAX_VALUE, Float.POSITIVE_INFINITY,
                Float.POSITIVE_INFINITY, Integer.MAX_VALUE));
        setUnlocalizedName("ultimate_sword");
        setRegistryName("ultimate_sword");
        setMaxDamage(0);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        UltimateUtil.kill(entity);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            UltimateUtil.addUltimatePlayer(player);
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            for (Entity e : entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving,
                    entityLiving.getEntityBoundingBox().grow(8))) {
                if (e instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) e;
                    ItemStack itemStack = entityItem.getItem();
                    player.onItemPickup(entityItem, itemStack.getCount());
                    player.addItemStackToInventory(itemStack);
                    entityItem.setDead();
                }

                if (e instanceof EntityXPOrb) {
                    EntityXPOrb xpOrb = (EntityXPOrb) e;
                    int value = xpOrb.getXpValue();
                    player.onItemPickup(xpOrb, value);
                    player.addExperience(maxStackSize);
                    xpOrb.setDead();
                }

                if (e instanceof EntityArrow) {
                    EntityArrow entityArrow = (EntityArrow) e;
                    player.onItemPickup(entityArrow, 1);
                    entityArrow.setDead();
                    if (!entityArrow.onGround) {
                        EntityLightningBolt lightningBolt = new EntityLightningBolt(player.world,
                                entityArrow.posX, entityArrow.posY, entityArrow.posZ, false);
                        lightningBolt.world.spawnEntity(lightningBolt);
                    }
                }

                player.onCriticalHit(e);
                player.onEnchantmentCritical(e);
                UltimateUtil.kill(e);
                playHurtSound(player, e);
            }
        }

        return super.onEntitySwing(entityLiving, stack);
    }

    private void playHurtSound(EntityPlayer player, Entity e) {
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
        player.world.playSound(
                player, e.posX, e.posY, e.posZ,
                SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LudicrousText.FABLOUSNESS.make("Ultimate Sword");
    }
}
