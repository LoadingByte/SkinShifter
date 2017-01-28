
package de.unratedfilms.skinshifter.client.gui;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.skinshifter.client.skin.services.SkinApplierService;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class PlayerDisplay extends ContextHelperWidgetAdapter implements WidgetFlexible {

    private Skin customSkin;

    public Skin getCustomSkin() {

        return customSkin;
    }

    public void setCustomSkin(Skin customSkin) {

        this.customSkin = customSkin;
    }

    @Override
    public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        float scaleX = getWidth() / (33f / 30f);
        float scaleY = getHeight() / (62f / 30f);
        float finalScale = Math.min(scaleX, scaleY);

        float offsetX = finalScale * (17f / 30f);
        float offsetY = finalScale * (59f / 30f);

        // Center the player on the axis whose side length is larger than the actual player
        float playerWidth = finalScale * (33f / 30f);
        float playerHeight = finalScale * (62f / 30f);
        if ((float) getWidth() / getHeight() > 33f / 62f) {
            offsetX += (getWidth() - playerWidth) / 2;
        } else {
            offsetY += (getHeight() - playerHeight) / 2;
        }

        // The mouse position relative to the head of the player
        float relativeMouseX = - (lmx - (getX() + offsetX));
        float relativeMouseY = - (lmy - (getY() + offsetY - playerHeight * 0.8f));

        String oldSkinType = MC.player.getSkinType();
        ResourceLocation oldSkinResource = MC.player.getLocationSkin();
        if (customSkin != null) {
            SkinApplierService.setSkinTo(MC.player, customSkin);
        }

        /*
         * The reason for why this is here is actually quite interesting.
         * Before the player display widget is drawn, other widgets probably have already been rendered.
         * With a high chance, those widgets changed the GL color to fit their needs, especially if they rendered text or custom shapes without textures.
         * That means that the GL color might still set to the one used by the last rendered widget.
         * Because that GL color is therefore unpredictable, we really need to reset it here so that it doesn't mess up our player render down below.
         */
        GlStateManager.color(1, 1, 1, 1);

        // This method is normally used to draw the small player in the middle of the inventory screen
        GuiInventory.drawEntityOnScreen(getX() + (int) offsetX, getY() + (int) offsetY, (int) finalScale, relativeMouseX, relativeMouseY, MC.player);

        if (customSkin != null) {
            SkinApplierService.setSkinTo(MC.player, oldSkinType, oldSkinResource);
        }
    }

}
