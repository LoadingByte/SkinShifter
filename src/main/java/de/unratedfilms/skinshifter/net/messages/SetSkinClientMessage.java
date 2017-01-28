
package de.unratedfilms.skinshifter.net.messages;

import org.apache.commons.lang3.Validate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.unratedfilms.skinshifter.client.skin.services.SkinApplierService;
import de.unratedfilms.skinshifter.common.skin.Skin;
import de.unratedfilms.skinshifter.common.skin.services.SkinEncoderService;
import io.netty.buffer.ByteBuf;

/**
 * This message tells the client that it should set the {@link Skin} of a given player to a given one.
 */
public class SetSkinClientMessage implements IMessage {

    private String playerName;
    private Skin   skin;

    public SetSkinClientMessage() {

    }

    public SetSkinClientMessage(String playerName, Skin skin) {

        Validate.notBlank(playerName);
        Validate.notNull(skin);

        this.playerName = playerName;
        this.skin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        playerName = ByteBufUtils.readUTF8String(buf);
        skin = SkinEncoderService.readSkinBinary(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, playerName);
        SkinEncoderService.writeSkinBinary(buf, skin);
    }

    public static class SetSkinClientMessageHandler implements IMessageHandler<SetSkinClientMessage, IMessage> {

        @Override
        @SideOnly (Side.CLIENT)
        public IMessage onMessage(SetSkinClientMessage message, MessageContext ctx) {

            AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getMinecraft().world.getPlayerEntityByName(message.playerName);
            SkinApplierService.setSkinTo(player, message.skin);

            // No reply
            return null;
        }

    }

}
