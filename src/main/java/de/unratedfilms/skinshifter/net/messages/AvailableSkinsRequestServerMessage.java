
package de.unratedfilms.skinshifter.net.messages;

import java.util.Set;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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

            // The provider method is functional and therefore thread-safe; we do not need to care about threading here
            Set<Skin> availableSkins = SkinProviderService.getAvailableSkins();
            Skin[] availableSkinsArr = availableSkins.toArray(new Skin[availableSkins.size()]);

            return new AvailableSkinsResponseClientMessage(availableSkinsArr);
        }

    }

}
