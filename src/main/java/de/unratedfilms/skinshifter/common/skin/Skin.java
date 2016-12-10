
package de.unratedfilms.skinshifter.common.skin;

import java.awt.image.BufferedImage;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Skin implements Comparable<Skin> {

    private static final int    TEXTURE_WIDTH  = 64;
    private static final int    TEXTURE_HEIGHT = 32;

    private final String        name;
    private final BufferedImage texture;

    public Skin(String name, BufferedImage texture) {

        this.name = name;

        BufferedImage croppedTexture = texture.getSubimage(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        this.texture = croppedTexture;
    }

    public String getName() {

        return name;
    }

    public BufferedImage getTexture() {

        return texture;
    }

    @Override
    public int compareTo(Skin o) {

        return name.compareTo(o.name);
    }

    @Override
    public int hashCode() {

        return HashCodeBuilder.reflectionHashCode(this, "texture");
    }

    @Override
    public boolean equals(Object obj) {

        return EqualsBuilder.reflectionEquals(this, obj, "texture");
    }

    @Override
    public String toString() {

        return "Skin '" + name + "'";
    }

}
