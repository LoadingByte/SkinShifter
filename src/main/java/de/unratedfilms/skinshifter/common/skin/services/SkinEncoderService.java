
package de.unratedfilms.skinshifter.common.skin.services;

import static de.unratedfilms.skinshifter.Consts.LOGGER;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.Validate;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import de.unratedfilms.skinshifter.common.skin.Skin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public class SkinEncoderService {

    public static Skin readSkinBinary(ByteBuf from) {

        String name = ByteBufUtils.readUTF8String(from);
        BufferedImage texture = readBufferedImageBinary(from);

        return new Skin(name, texture);
    }

    public static void writeSkinBinary(ByteBuf to, Skin skin) {

        ByteBufUtils.writeUTF8String(to, skin.getName());
        writeBufferedImageBinary(to, skin.getTexture(), "png");
    }

    private static BufferedImage readBufferedImageBinary(ByteBuf from) {

        // Read the length of the following image byte array
        int length = ByteBufUtils.readVarInt(from, 4);

        try {
            // Read the image byte array itself
            return ImageIO.read(new ByteBufInputStream(from.readBytes(length)));
        } catch (IOException e) {
            LOGGER.error("Unable to decode BufferedImage from incoming ByteBuf", e);
            return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private static void writeBufferedImageBinary(ByteBuf to, BufferedImage image, String formatName) {

        byte[] imageBytes;
        try {
            // Write the image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, formatName, baos);
            imageBytes = baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Unable to encode BufferedImage into outgoing ByteBuf", e);
            return;
        }

        Validate.isTrue(ByteBufUtils.varIntByteCount(imageBytes.length) <= 4, "The BufferedImage is too long for this encoding.");

        // Write the length of the byte array, then the byte array itself
        ByteBufUtils.writeVarInt(to, imageBytes.length, 4);
        to.writeBytes(imageBytes);
    }

    private SkinEncoderService() {}

}
