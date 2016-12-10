
package de.unratedfilms.skinshifter.net.messages;

import org.apache.commons.lang3.Validate;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinRecorderService;
import io.netty.buffer.ByteBuf;

/**
 * This message tells the server that it should send the skin of the specified player to the sending player via a {@link SetSkinClientMessage}.
 */
public class PollSkinServerMessage implements IMessage {

    private String playerName;

    public PollSkinServerMessage() {

    }

    public PollSkinServerMessage(String playerName) {

        Validate.notBlank(playerName);

        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, playerName);
    }

    public static class PollSkinServerMessageHandler implements IMessageHandler<PollSkinServerMessage, SetSkinClientMessage> {

        @Override
        public SetSkinClientMessage onMessage(PollSkinServerMessage message, MessageContext ctx) {

            Skin skin = SkinRecorderService.getRecordedSkinOf(message.playerName);

            if (skin != null) {
                SetSkinClientMessage reply = new SetSkinClientMessage(message.playerName, skin);
                return reply;
            } else {
                return null;
            }
        }

    }

}
