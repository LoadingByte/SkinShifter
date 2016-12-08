
package de.unratedfilms.skinshifter.client.skin;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class SkinApplier {

    public static void applySkinTo(AbstractClientPlayer player, ResourceLocation skinResource) {

        player.func_152121_a(MinecraftProfileTexture.Type.SKIN, skinResource);
    }

    public static void applySkinTo(AbstractClientPlayer player, Skin skin) {

        // Allocate a new texture resource location and put a dynamic texture with the skin's image in there
        ResourceLocation skinResource = new ResourceLocation(Consts.MOD_ID, "skins/" + skin.getName());
        Minecraft.getMinecraft().getTextureManager().loadTexture(skinResource, new DynamicTexture(skin.getTexture()));

        // Set the skin of the player to that new texture resource location
        applySkinTo(player, skinResource);
    }

    private SkinApplier() {}

}
