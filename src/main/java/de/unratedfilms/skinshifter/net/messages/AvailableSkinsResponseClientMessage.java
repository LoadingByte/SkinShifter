
package de.unratedfilms.skinshifter.net.messages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.unratedfilms.skinshifter.client.gui.SkinSelectionScreen;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinEncoderService;
import de.unratedfilms.skinshifter.common.skin.services.SkinProviderService;
import io.netty.buffer.ByteBuf;

/**
 * After the client asked the server for all available {@link Skin}s using an {@link AvailableSkinsRequestServerMessage},
 * the server answers that request with this message.
 */
public class AvailableSkinsResponseClientMessage implements IMessage {

    private Skin[] availableSkins;

    public AvailableSkinsResponseClientMessage() {

    }

    public AvailableSkinsResponseClientMessage(Skin[] availableSkins) {

        Validate.notNull(availableSkins);
        this.availableSkins = availableSkins;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        availableSkins = new Skin[buf.readInt()];
        for (int i = 0; i < availableSkins.length; i++) {
            availableSkins[i] = SkinEncoderService.readSkinBinary(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(availableSkins.length);
        for (Skin availableSkin : availableSkins) {
            SkinEncoderService.writeSkinBinary(buf, availableSkin);
        }
    }

    public static class AvailableSkinsResponseClientMessageHandler implements IMessageHandler<AvailableSkinsResponseClientMessage, IMessage> {

        @Override
        @SideOnly (Side.CLIENT)
        public IMessage onMessage(AvailableSkinsResponseClientMessage message, MessageContext ctx) {

            Set<Skin> allAvailableSkins = new HashSet<>();

            // Consider the skins which are stored on the client
            allAvailableSkins.addAll(SkinProviderService.getAvailableSkins());

            // And, of course, the skins which are stored on the server
            allAvailableSkins.addAll(Arrays.asList(message.availableSkins));

            Minecraft.getMinecraft().addScheduledTask(() -> {
                // Open a create program GUI that displays the skins
                Minecraft.getMinecraft().displayGuiScreen(new SkinSelectionScreen(null, allAvailableSkins));
            });

            // No reply
            return null;
        }

    }

}
