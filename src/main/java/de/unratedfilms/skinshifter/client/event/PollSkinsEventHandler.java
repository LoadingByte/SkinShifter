
package de.unratedfilms.skinshifter.client.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.skinshifter.net.NetworkService;
import de.unratedfilms.skinshifter.net.messages.PollSkinServerMessage;

/**
 * This event handler is responsible for making sure that other players, who just entered the world of the client, are properly reskinned.
 */
public class PollSkinsEventHandler {

    /*
     * Since a player's skin is reset if that player is (re)added to my world (skin setting mechanism is placed in constructor of AbstractClientPlayer),
     * I have to poll that player's custom skin each time he is (re)added.
     */
    @SubscribeEvent
    public void onPlayerSpawn(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayer) {
            String spawnedPlayerName = event.getEntity().getName();
            PollSkinServerMessage message = new PollSkinServerMessage(spawnedPlayerName);
            NetworkService.DISPATCHER.sendToServer(message);
        }
    }

}
