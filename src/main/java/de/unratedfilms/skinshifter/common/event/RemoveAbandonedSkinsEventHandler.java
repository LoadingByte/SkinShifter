
package de.unratedfilms.skinshifter.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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

        SkinRecorderService.recordSkinClear(event.player.getName());
    }

}
