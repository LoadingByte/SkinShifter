
package de.unratedfilms.skinshifter.client.gui;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.skinshifter.client.skin.SkinApplier;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class PlayerDisplay extends ContextHelperWidgetAdapter implements WidgetFlexible {

    private Skin skin;

    public Skin getSkin() {

        return skin;
    }

    public void setSkin(Skin skin) {

        this.skin = skin;
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

        ResourceLocation oldSkinResource = MC.thePlayer.getLocationSkin();
        if (skin != null) {
            SkinApplier.applySkinTo(MC.thePlayer, skin);
        }

        GuiInventory.func_147046_a(getX() + (int) offsetX, getY() + (int) offsetY, (int) finalScale, relativeMouseX, relativeMouseY, MC.thePlayer);

        if (skin != null) {
            SkinApplier.applySkinTo(MC.thePlayer, oldSkinResource);
        }
    }

}
