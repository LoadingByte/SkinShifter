
package de.unratedfilms.skinshifter.client.keys;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.unratedfilms.skinshifter.net.NetworkService;
import de.unratedfilms.skinshifter.net.messages.AvailableSkinsRequestServerMessage;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.openSkinSelectionScreen.isPressed()) {
            // Request the available skins which are located on the server; the incoming response will automatically open up a create program GUI
            NetworkService.DISPATCHER.sendToServer(new AvailableSkinsRequestServerMessage());
        }
    }

}
