
package de.unratedfilms.skinshifter.net.messages;

import java.util.Optional;
import org.apache.commons.lang3.Validate;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinRecorderService;
import io.netty.buffer.ByteBuf;

/**
 * This message tells the server that it should send the skin of the specified player to the sending player via a {@link SetSkinClientMessage} or {@link ClearSkinClientMessage}.
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

    public static class PollSkinServerMessageHandler implements IMessageHandler<PollSkinServerMessage, IMessage> {

        @Override
        public IMessage onMessage(PollSkinServerMessage message, MessageContext ctx) {

            Optional<Skin> skin = SkinRecorderService.getRecordedSkinOf(message.playerName);

            if (skin.isPresent()) {
                return new SetSkinClientMessage(message.playerName, skin.get());
            } else {
                return new ClearSkinClientMessage(message.playerName);
            }
        }

    }

}
