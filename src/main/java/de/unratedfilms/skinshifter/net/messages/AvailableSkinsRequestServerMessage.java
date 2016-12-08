
package de.unratedfilms.skinshifter.net.messages;

import java.util.Set;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinProviderService;
import io.netty.buffer.ByteBuf;

/**
 * The client asks the server for all {@link Skin}s it has available.
 * The server then responds with an {@link AvailableSkinsResponseClientMessage}.
 */
public class AvailableSkinsRequestServerMessage implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class AvailableSkinsRequestServerMessageHandler implements IMessageHandler<AvailableSkinsRequestServerMessage, AvailableSkinsResponseClientMessage> {

        @Override
        public AvailableSkinsResponseClientMessage onMessage(AvailableSkinsRequestServerMessage message, MessageContext ctx) {

            Set<Skin> availableSkins = SkinProviderService.getAvailableSkins();
            Skin[] availableSkinsArr = availableSkins.toArray(new Skin[availableSkins.size()]);

            return new AvailableSkinsResponseClientMessage(availableSkinsArr);
        }

    }

}
