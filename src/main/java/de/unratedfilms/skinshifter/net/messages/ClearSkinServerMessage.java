
package de.unratedfilms.skinshifter.net.messages;

import static de.unratedfilms.skinshifter.Consts.LOGGER;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinRecorderService;
import de.unratedfilms.skinshifter.net.NetworkService;
import io.netty.buffer.ByteBuf;

/**
 * This message tells the server that it should revert the {@link Skin} of the sending player to his default one.
 */
public class ClearSkinServerMessage implements IMessage {

    public ClearSkinServerMessage() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class ClearSkinServerMessageHandler implements IMessageHandler<ClearSkinServerMessage, IMessage> {

        @Override
        public IMessage onMessage(ClearSkinServerMessage message, MessageContext ctx) {

            String sourcePlayerName = ctx.getServerHandler().playerEntity.getCommandSenderName();

            LOGGER.info("Player '{}' cleared his skin back to the default", sourcePlayerName);

            // Remember the fact that the player's skin is now his default again
            SkinRecorderService.recordSkinClear(sourcePlayerName);

            // Broadcast the skin change back to all players
            ClearSkinClientMessage reply = new ClearSkinClientMessage(sourcePlayerName);
            NetworkService.DISPATCHER.sendToAll(reply);

            // No other reply
            return null;
        }

    }

}
