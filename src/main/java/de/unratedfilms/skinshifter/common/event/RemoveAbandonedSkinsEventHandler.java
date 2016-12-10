
package de.unratedfilms.skinshifter.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import de.unratedfilms.skinshifter.common.skin.services.SkinRecorderService;

/**
 * This event handler deletes {@link SkinRecorderService recorded} skins as soon as the recording player leaves the game.
 */
public class RemoveAbandonedSkinsEventHandler {

    /*
     * Sadly, this doesn't work for singleplayer. But I have no idea what does ...
     */
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

        SkinRecorderService.recordSkinChange(event.player.getCommandSenderName(), null);
    }

}
