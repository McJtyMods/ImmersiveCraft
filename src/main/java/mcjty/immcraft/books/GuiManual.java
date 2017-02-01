package mcjty.immcraft.books;

import mcjty.immcraft.ImmersiveCraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiManual extends GuiScreen {

    private static final int WIDTH = 253;
    private static final int HEIGHT = 210;
    private int guiLeft;
    private int guiTop;

    private ResourceLocation manualText;

    private static final ResourceLocation background = new ResourceLocation(ImmersiveCraft.MODID, "textures/gui/manual_paper.png");

    public GuiManual() {
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
    }
}
