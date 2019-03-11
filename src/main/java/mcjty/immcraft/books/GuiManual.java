package mcjty.immcraft.books;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.book.IBook;
import mcjty.immcraft.blocks.book.BookStandTE;
import mcjty.immcraft.books.renderers.RenderElementText;
import mcjty.immcraft.setup.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiManual extends GuiScreen {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 220;
    private int guiLeft;
    private int guiTop;

    private ResourceLocation json;
    private List<BookPage> pages;
    private int pageNumber = 0;
    private String result = null;

    private static final ResourceLocation background = new ResourceLocation(ImmersiveCraft.MODID, "textures/gui/manual_paper.png");
    private static final ResourceLocation backgroundFront = new ResourceLocation(ImmersiveCraft.MODID, "textures/gui/manual_front.png");

    public GuiManual() {
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack book = player.getHeldItemMainhand();
        if (!book.isEmpty() && book.getItem() instanceof IBook) {
            json = ((IBook) book.getItem()).getJson();
            BookParser parser = new BookParser();
            pages = parser.parse(json, 768, 900);
            pageNumber = 0;
            result = null;
        } else {
            json = null;    // Shouldn't be possible
            pages = new ArrayList<>();
            pages.add(new BookPage());
            RenderSection section = new RenderSection("Error");
            TextElementFormat fmt = new TextElementFormat("red,bold");
            section.addElement(new RenderElementText("Error!", 10, 10, (int) ClientProxy.font.getWidth("Error!"), (int) ClientProxy.font.getHeight(), fmt));
            pages.get(0).addSection(section);
            pageNumber = 0;
            result = null;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        if (result != null) {
            if ("<".equals(result)) {
                pageDec();
            } else if (">".equals(result)) {
                pageInc();
            } else if ("^".equals(result)) {
                pageFront();
            } else {
                int number = BookStandTE.findPageForSection(pages, result);
                if (number != -1 && number != pageNumber) {
                    pageNumber = number;
                    playPageTurn();
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_SPACE || keyCode == Keyboard.KEY_RIGHT) {
            pageInc();
        } else if (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_BACK) {
            pageDec();
        } else if (keyCode == Keyboard.KEY_HOME) {
            pageFront();
        }
    }

    private void pageFront() {
        if (pageNumber != 0) {
            pageNumber = 0;
            playPageTurn();
        }
    }

    private void pageInc() {
        if (pageNumber < pages.size()-1) {
            pageNumber++;
            playPageTurn();
        }
    }

    private void pageDec() {
        if (pageNumber > 0) {
            pageNumber--;
            playPageTurn();
        }
    }

    private void playPageTurn() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        BookStandTE.playPageTurn(Minecraft.getMinecraft().world, player.getPosition());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (pageNumber == 0) {
            mc.getTextureManager().bindTexture(backgroundFront);
        } else {
            mc.getTextureManager().bindTexture(background);
        }
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        float ix = (float) (mouseX - guiLeft) / WIDTH;
        float iy = (float) (mouseY - guiTop) / HEIGHT;
        if (ix < .15f) {
            result = "<";
        } else if (ix > 1-.1f) {
            result = ">";
        } else if (iy < .15f) {
            result = "^";
        } else {
            result = null;
        }
        String rc = BookRenderHelper.renderPageForGUI(pages, pageNumber, 1.0f, ix, iy, guiLeft, guiTop);
        if (rc != null) {
            result = rc;
        }
    }
}
