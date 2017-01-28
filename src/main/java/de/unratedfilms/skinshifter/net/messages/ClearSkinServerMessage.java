
package de.unratedfilms.skinshifter.net.messages;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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

            String sourcePlayerName = ctx.getServerHandler().playerEntity.getName();

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
