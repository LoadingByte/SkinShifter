
package de.unratedfilms.skinshifter.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.net.messages.AvailableSkinsRequestServerMessage;
import de.unratedfilms.skinshifter.net.messages.AvailableSkinsRequestServerMessage.AvailableSkinsRequestServerMessageHandler;
import de.unratedfilms.skinshifter.net.messages.AvailableSkinsResponseClientMessage;
import de.unratedfilms.skinshifter.net.messages.AvailableSkinsResponseClientMessage.AvailableSkinsResponseClientMessageHandler;
import de.unratedfilms.skinshifter.net.messages.ClearSkinClientMessage;
import de.unratedfilms.skinshifter.net.messages.ClearSkinClientMessage.ClearSkinClientMessageHandler;
import de.unratedfilms.skinshifter.net.messages.ClearSkinServerMessage;
import de.unratedfilms.skinshifter.net.messages.ClearSkinServerMessage.ClearSkinServerMessageHandler;
import de.unratedfilms.skinshifter.net.messages.PollSkinServerMessage;
import de.unratedfilms.skinshifter.net.messages.PollSkinServerMessage.PollSkinServerMessageHandler;
import de.unratedfilms.skinshifter.net.messages.SetSkinClientMessage;
import de.unratedfilms.skinshifter.net.messages.SetSkinClientMessage.SetSkinClientMessageHandler;
import de.unratedfilms.skinshifter.net.messages.SetSkinServerMessage;
import de.unratedfilms.skinshifter.net.messages.SetSkinServerMessage.SetSkinServerMessageHandler;

public class NetworkService {

    public static final SimpleNetworkWrapper DISPATCHER        = NetworkRegistry.INSTANCE.newSimpleChannel(Consts.MOD_ID);

    private static int                       nextDiscriminator = 0;

    public static void initialize() {

        // Available skins messages
        registerMessage(AvailableSkinsRequestServerMessageHandler.class, AvailableSkinsRequestServerMessage.class, Side.SERVER);
        registerMessage(AvailableSkinsResponseClientMessageHandler.class, AvailableSkinsResponseClientMessage.class, Side.CLIENT);

        // Set skin messages
        registerMessage(SetSkinServerMessageHandler.class, SetSkinServerMessage.class, Side.SERVER);
        registerMessage(SetSkinClientMessageHandler.class, SetSkinClientMessage.class, Side.CLIENT);

        // Clear skin messages
        registerMessage(ClearSkinServerMessageHandler.class, ClearSkinServerMessage.class, Side.SERVER);
        registerMessage(ClearSkinClientMessageHandler.class, ClearSkinClientMessage.class, Side.CLIENT);

        // Poll skin message
        registerMessage(PollSkinServerMessageHandler.class, PollSkinServerMessage.class, Side.SERVER);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {

        DISPATCHER.registerMessage(messageHandler, requestMessageType, nextDiscriminator, side);
        nextDiscriminator++;
    }

    private NetworkService() {

    }

}
