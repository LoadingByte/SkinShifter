
package de.unratedfilms.skinshifter.client.skin.services;

import static de.unratedfilms.skinshifter.Consts.LOGGER;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class SkinApplierService {

    public static void clearSkinToDefault(AbstractClientPlayer player) {

        NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID());

        try {
            /*
             * First, reset the skin (and all other textures) to the default by nulling everything.
             * That's the default state when a new player joins the game and his skin has not been loaded yet.
             * It's also the state the game stays in if the player didn't upload a custom one to the Mojang server.
             */
            setSkinType(playerInfo, null);
            getPlayerTextures(playerInfo).clear();

            /*
             * Now, tell the player info object that the player textures have not been loaded yet.
             * This will cause a complete reload of all textures, including the skin and skin model type, as soon as someone requests any one of those textures.
             * It doesn't do anything if there's no custom skin, so the default skin from the previous step would remain.
             * Note that this way of clearing the custom skin is quite elegant, since all the heavy lifting is done by the existing implementation itself.
             */
            setPlayerTexturesLoaded(playerInfo, false);
        } catch (UnableToAccessFieldException e) {
            LOGGER.fatal("Cannot clear and reload all the textures and skin data of the local player '{}'; are you using the appropriate version of {}?",
                    Consts.MOD_NAME, player.getName(), e);
        }
    }

    public static void setSkinTo(AbstractClientPlayer player, String skinType, ResourceLocation skinResource) {

        Validate.notBlank(skinType, "Can't apply a blank skin type to a player");
        Validate.notNull(skinResource, "Can't apply a null skin resource to a player");

        NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID());

        try {
            // First, adjust set the 'skinType' field to match the desired player model.
            setSkinType(playerInfo, skinType);

            // Then, put the custom skin resource into the 'playerTextures' map, replacing the player's real skin.
            getPlayerTextures(playerInfo).put(MinecraftProfileTexture.Type.SKIN, skinResource);
        } catch (UnableToAccessFieldException e) {
            LOGGER.fatal("Cannot manipulate the textures and skin data of the local player '{}' in order to change his skin; are you using the appropriate version of {}?",
                    Consts.MOD_NAME, player.getName(), e);
        }
    }

    public static void setSkinTo(AbstractClientPlayer player, Skin skin) {

        Validate.notNull(skin, "Can't apply a null skin to a player");

        // Allocate a new texture resource location and put a dynamic texture with the skin's image in there
        ResourceLocation skinResource = new ResourceLocation(Consts.MOD_ID, "skins/" + skin.getName());
        Minecraft.getMinecraft().getTextureManager().loadTexture(skinResource, new DynamicTexture(skin.getTexture()));

        // Set the skin of the player to that new texture resource location
        setSkinTo(player, skin.getModel().toString(), skinResource);
    }

    // ----- Reflection Helper Methods -----

    private static Map<MinecraftProfileTexture.Type, ResourceLocation> getPlayerTextures(NetworkPlayerInfo target) {

        return ReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, target, 1 /* playerTextures */);
    }

    private static void setSkinType(NetworkPlayerInfo target, String skinType) {

        ReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, target, skinType, 5 /* skinType */);
    }

    private static void setPlayerTexturesLoaded(NetworkPlayerInfo target, boolean playerTexturesLoaded) {

        ReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, target, playerTexturesLoaded, 4 /* playerTexturesLoaded */);
    }

    private SkinApplierService() {}

}
