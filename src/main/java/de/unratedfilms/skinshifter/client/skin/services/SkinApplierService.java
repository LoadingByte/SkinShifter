
package de.unratedfilms.skinshifter.client.skin.services;

import org.apache.commons.lang3.Validate;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class SkinApplierService {

    public static void clearSkinToDefault(AbstractClientPlayer player) {

        // First reset the skin to Steve, which is the default skin if the player didn't upload a custom one to the Mojang server
        player.func_152121_a(MinecraftProfileTexture.Type.SKIN, null);

        /*
         * Then try to fetch the custom skin from the mojang server
         * This doesn't do anything if there's no custom skin, so the Steve skin from the previous step would remain.
         * Note that this snippet has been copied from the constructor of AbstractClientPlayer.
         */
        SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();
        skinManager.func_152790_a(player.getGameProfile(), player, true);
    }

    public static void setSkinTo(AbstractClientPlayer player, ResourceLocation skinResource) {

        Validate.notNull(skinResource, "Can't apply a null skin resource to a player");

        player.func_152121_a(MinecraftProfileTexture.Type.SKIN, skinResource);
    }

    public static void setSkinTo(AbstractClientPlayer player, Skin skin) {

        Validate.notNull(skin, "Can't apply a null skin to a player");

        // Allocate a new texture resource location and put a dynamic texture with the skin's image in there
        ResourceLocation skinResource = new ResourceLocation(Consts.MOD_ID, "skins/" + skin.getName());
        Minecraft.getMinecraft().getTextureManager().loadTexture(skinResource, new DynamicTexture(skin.getTexture()));

        // Set the skin of the player to that new texture resource location
        setSkinTo(player, skinResource);
    }

    private SkinApplierService() {}

}
