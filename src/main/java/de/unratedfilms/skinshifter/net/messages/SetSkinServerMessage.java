
package de.unratedfilms.skinshifter.net.messages;

import org.apache.commons.lang3.Validate;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinEncoderService;
import de.unratedfilms.skinshifter.common.skin.services.SkinRecorderService;
import de.unratedfilms.skinshifter.net.NetworkService;
import io.netty.buffer.ByteBuf;

/**
 * This message tells the server that it should set the {@link Skin} of the sending player to a given one.
 */
public class SetSkinServerMessage implements IMessage {

    private Skin skin;

    public SetSkinServerMessage() {

    }

    public SetSkinServerMessage(Skin skin) {

        Validate.notNull(skin);

        this.skin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        skin = SkinEncoderService.readSkinBinary(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        SkinEncoderService.writeSkinBinary(buf, skin);
    }

    public static class SetSkinServerMessageHandler implements IMessageHandler<SetSkinServerMessage, IMessage> {

        @Override
        public IMessage onMessage(SetSkinServerMessage message, MessageContext ctx) {

            String sourcePlayerName = ctx.getServerHandler().playerEntity.getName();

            // Remember the chosen skin
            SkinRecorderService.recordSkinSet(sourcePlayerName, message.skin);

            // Broadcast the skin change back to all players
            SetSkinClientMessage reply = new SetSkinClientMessage(sourcePlayerName, message.skin);
            NetworkService.DISPATCHER.sendToAll(reply);

            // No other reply
            return null;
        }

    }

}
