package ultimate.client.event;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ultimate.common.item.ItemUltimateSword;
import ultimate.common.util.text.LudicrousText;

public class ClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if ((event.getItemStack().getItem() instanceof ItemUltimateSword)) {
            List<String> tooltip = event.getToolTip();
            int size = tooltip.size();
            String attackDamage = I18n.format("attribute.name.generic.attackDamage");
            String attackSpeed = I18n.format("attribute.name.generic.attackSpeed");
            String replacementAttackDamage = new StringBuilder()
                    .append(' ')
                    .append(LudicrousText.FABLOUSNESS.make("Infinity"))
                    .append(' ')
                    .append(TextFormatting.GRAY.toString())
                    .append(attackDamage).toString();
            String replacementAttackSpeed = new StringBuilder()
                    .append(' ')
                    .append(LudicrousText.FABLOUSNESS.make("Infinity"))
                    .append(' ')
                    .append(TextFormatting.GRAY.toString())
                    .append(attackSpeed).toString();
            for (int i = 0; i < size; i++) {
                String line = tooltip.get(i);
                if (line.contains(attackDamage)) {
                    tooltip.set(i, replacementAttackDamage);
                    continue;
                }
                if (line.contains(attackSpeed)) {
                    tooltip.set(i, replacementAttackSpeed);
                    continue;
                }
            }
        }
    }

}
