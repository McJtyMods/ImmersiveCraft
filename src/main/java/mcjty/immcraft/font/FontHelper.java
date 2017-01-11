package mcjty.immcraft.font;

/**
 * TrueTyper: Open Source TTF implementation for Minecraft.
 * Copyright (C) 2013 - Mr_okushama
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

public class FontHelper {

    private static String formatEscape = "\u00A7";


    public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, float... rgba) {
        drawString(s, x, y, font, scaleX, scaleY, 0, rgba);

    }

    public static void drawString(String s, float x, float y, TrueTypeFont font, float scaleX, float scaleY, float rotationZ, float... rgba) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        if (mc.gameSettings.hideGUI) {
            return;
        }
        int amt = 1;
        if (sr.getScaleFactor() == 1) {
            amt = 2;
        }

        FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, matrixData);
        FontHelper.set2DMode(matrixData);
        GlStateManager.pushMatrix();
        y = mc.displayHeight - (y * sr.getScaleFactor()) - (((font.getLineHeight() / amt)));
        float tx = (x * sr.getScaleFactor()) + (font.getWidth(s) / 2);
        float tranx = tx + 2;
        float trany = y + (font.getLineHeight() / 2);
        GlStateManager.translate(tranx, trany, 0);
        GlStateManager.rotate(rotationZ, 0f, 0f, 1f);
        GlStateManager.translate(-tranx, -trany, 0);


        GlStateManager.enableBlend();
        if (s.contains(formatEscape)) {
            String[] pars = s.split(formatEscape);
            float totalOffset = 0;
            for (int i = 0; i < pars.length; i++) {
                String par = pars[i];
                float[] c = rgba;
                if (i > 0) {
                    c = Formatter.getFormatted(par.charAt(0));
                    par = par.substring(1, par.length());
                }
                font.drawString((x * sr.getScaleFactor() + totalOffset), y, par, scaleX / amt, scaleY / amt, c);
                totalOffset += font.getWidth(par);
            }
        } else {
            font.drawString((x * sr.getScaleFactor()), y, s, scaleX / amt, scaleY / amt, rgba);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        FontHelper.set3DMode();
    }

    private static void set2DMode(FloatBuffer matrixData) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GL11.glLoadMatrix(matrixData);

        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, mc.displayWidth, 0, mc.displayHeight, -1, 1);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();

        Matrix4f matrix = new Matrix4f();
        matrix.load(matrixData);
        GlStateManager.translate(matrix.m30 * sr.getScaleFactor(), -matrix.m31 * sr.getScaleFactor(), 0f);

    }

    private static void set3DMode() {
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        mc.entityRenderer.setupOverlayRendering();
    }

}