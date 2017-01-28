
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
import io.netty.buffer.ByteBuf;

/**
 * This message tells the client that it should revert the {@link Skin} of a given player to his default one.
 */
public class ClearSkinClientMessage implements IMessage {

    private String playerName;

    public ClearSkinClientMessage() {

    }

    public ClearSkinClientMessage(String playerName) {

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

    public static class ClearSkinClientMessageHandler implements IMessageHandler<ClearSkinClientMessage, IMessage> {

        @Override
        @SideOnly (Side.CLIENT)
        public IMessage onMessage(ClearSkinClientMessage message, MessageContext ctx) {

            Minecraft.getMinecraft().addScheduledTask(() -> {
                AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getMinecraft().world.getPlayerEntityByName(message.playerName);
                SkinApplierService.clearSkinToDefault(player);
            });

            // No reply
            return null;
        }

    }

}
