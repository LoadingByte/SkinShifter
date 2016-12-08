
package de.unratedfilms.skinshifter.net.messages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.unratedfilms.skinshifter.client.gui.SkinSelectionScreen;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.SkinEncoder;
import de.unratedfilms.skinshifter.common.skin.SkinProvider;
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
            availableSkins[i] = SkinEncoder.readSkinBinary(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(availableSkins.length);
        for (Skin availableSkin : availableSkins) {
            SkinEncoder.writeSkinBinary(buf, availableSkin);
        }
    }

    public static class AvailableSkinsResponseClientMessageHandler implements IMessageHandler<AvailableSkinsResponseClientMessage, IMessage> {

        @Override
        @SideOnly (Side.CLIENT)
        public IMessage onMessage(AvailableSkinsResponseClientMessage message, MessageContext ctx) {

            Set<Skin> allAvailableSkins = new HashSet<>();

            // Consider the skins which are stored on the client
            allAvailableSkins.addAll(SkinProvider.getAvailableSkins());

            // And, of course, the skins which are stored on the server
            allAvailableSkins.addAll(Arrays.asList(message.availableSkins));

            // Open a create program GUI that displays the skins
            Minecraft.getMinecraft().displayGuiScreen(new SkinSelectionScreen(null, allAvailableSkins));

            // No reply
            return null;
        }

    }

}
