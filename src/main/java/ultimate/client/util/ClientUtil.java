package ultimate.client.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class ClientUtil {
    private static GuiScreen lastGuiScreen;
    private static final List<Class<?>> blackList = Lists.newArrayList();

    private ClientUtil() {
    }

    public static boolean isGameOverGuiScreen(@Nullable GuiScreen screen) {
        try {
            if (screen == null) {
                return false;
            }
            if (blackList.contains(screen.getClass())) {
                return true;
            }

            if (lastGuiScreen != null) {
                if (!screen.getClass().getName().contains("net.minecraft")
                        && lastGuiScreen.getClass() == screen.getClass()) {
                    blackList.add(lastGuiScreen.getClass());
                    Minecraft minecraft = Minecraft.getMinecraft();
                    if (minecraft.player != null) {
                        minecraft.player.sendMessage(
                                new TextComponentString(new StringBuilder()
                                        .append("[Ultimate] GameOverGui(")
                                        .append(screen.getClass().getSimpleName())
                                        .append(") Detected.").toString()));
                    }
                    return true;
                }
            }
            lastGuiScreen = screen;
            if (screen instanceof GuiGameOver) {
                return true;
            }
            if (hasDeadButton(screen)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean hasDeadButton(GuiScreen screen) {
        try {
            Field field = ObfuscationReflectionHelper
                    .findField(screen.getClass(), "field_146292_n");
            // Bypass Java Language access check (improve performance)
            field.setAccessible(true);

            return ((ArrayList<GuiButton>) field.get(screen)).stream().anyMatch(ClientUtil::isDeadButton);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDeadButton(GuiButton button) {
        return button.displayString.equals(I18n.format("deathScreen.respawn", new Object[0]))
                || button.displayString
                        .equals(I18n.format("deathScreen.respawn", new Object[0]));
    }
}
