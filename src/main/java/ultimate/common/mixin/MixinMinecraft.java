package ultimate.common.mixin;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Timer;
import net.minecraftforge.client.GuiIngameForge;
import ultimate.UltimateMod;
import ultimate.client.util.ClientUtil;
import ultimate.common.util.UltimateUtil;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft {
    private Minecraft minecraft = (Minecraft) (Object) this;
    private static GuiIngameForge guiIngameForge;

    private long nanoTime;
    private EntityRenderer lastAvailableEntityRenderer;
    @Shadow
    private WorldClient world;

    @Shadow
    private EntityPlayerSP player;

    @Shadow
    @Nullable
    private GuiScreen currentScreen;

    @Shadow
    private GameSettings gameSettings;

    @Shadow
    private SoundHandler mcSoundHandler;

    @Shadow
    private GuiIngame ingameGUI;

    @Shadow
    private boolean skipRenderWorld;

    @Shadow
    private float renderPartialTicksPaused;

    @Shadow
    @Final
    private Timer timer;

    @Shadow
    private boolean isGamePaused;

    @Shadow
    protected abstract void setIngameFocus();

    @Shadow
    protected abstract void setIngameNotInFocus();

    @Inject(method = "runGameLoop", at = @At("HEAD"), cancellable = true)
    private void runGameLoop(CallbackInfo info) {
        try {
            if (minecraft.ingameGUI.getClass() != GuiIngameForge.class) {
                UltimateMod.getLogger().fatal("Invalid ingame-gui detected.");
                if (guiIngameForge == null) {
                    guiIngameForge = new GuiIngameForge(minecraft);
                }

                minecraft.ingameGUI = guiIngameForge;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        nanoTime = System.nanoTime();
    }

    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;updateCameraAndRender(FJ)V"))
    private void redirect$runGameLoop() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.ingameGUI.getClass() != GuiIngameForge.class) {
            UltimateMod.getLogger().fatal("Invalid ingame-gui detected.");
            if (guiIngameForge == null) {
                guiIngameForge = new GuiIngameForge(minecraft);
            }

            minecraft.ingameGUI = guiIngameForge;
        }

        if (minecraft.entityRenderer.getClass() != EntityRenderer.class) {
            UltimateMod.getLogger().fatal("Invalid entity renderer detected.");
            EntityRenderer entityRenderer = lastAvailableEntityRenderer;
            minecraft.entityRenderer = entityRenderer;
            entityRenderer.updateCameraAndRender(
                    isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks, nanoTime);
        } else {
            lastAvailableEntityRenderer = minecraft.entityRenderer;
            minecraft.entityRenderer.updateCameraAndRender(
                    isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks, nanoTime);
        }
    }

    /**
     * @author Minsk
     * @reason Bypass warning
     */
    @Overwrite
    public void displayGuiScreen(@Nullable GuiScreen guiScreenIn) {
        EntityPlayerSP player = minecraft.player;

        boolean isUltimate = player != null && UltimateUtil.isUltimatePlayer(player);
        if (isUltimate && ClientUtil.isGameOverGuiScreen(guiScreenIn)) {
            return;
        }

        if (guiScreenIn == null && this.world == null) {
            guiScreenIn = new GuiMainMenu();
        } else if (!isUltimate && guiScreenIn == null && this.player.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver(null);
        }

        GuiScreen old = this.currentScreen;
        net.minecraftforge.client.event.GuiOpenEvent event = new net.minecraftforge.client.event.GuiOpenEvent(
                guiScreenIn);

        if (!isUltimate && net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            return;

        guiScreenIn = event.getGui();
        if (old != null && guiScreenIn != old) {
            old.onGuiClosed();
        }

        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();

            while (Mouse.next()) {
                ;
            }

            while (Keyboard.next()) {
                ;
            }

            ScaledResolution scaledresolution = new ScaledResolution((Minecraft) (Object) this);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution((Minecraft) (Object) this, i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    @ModifyConstant(method = "createDisplay", constant = @Constant(stringValue = "Minecraft 1.12.2"))
    public String createDisplay(String constant) {
        return "圈大钱v2.0 | Minecraft 1.12.2";
    }
}
