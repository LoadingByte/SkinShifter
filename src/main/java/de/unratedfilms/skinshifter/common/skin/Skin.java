
package de.unratedfilms.skinshifter.common.skin;

import java.awt.image.BufferedImage;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Skin implements Comparable<Skin> {

    private static final int TEXTURE_WIDTH  = 64;
    private static final int TEXTURE_HEIGHT = 64;

    public static enum Model {

        DEFAULT, SLIM;

        @Override
        public String toString() {

            return name().toLowerCase();
        }

    }

    private final String        name;
    private final Model         model;
    private final BufferedImage texture;

    public Skin(String name, Model model, BufferedImage texture) {

        Validate.notBlank(name, "Skin name cannot be blank");
        Validate.notNull(model, "Skin model cannot be null");
        Validate.notNull(texture, "Skin texture cannot be null");

        this.name = name;
        this.model = model;

        BufferedImage croppedTexture = texture.getSubimage(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        this.texture = croppedTexture;
    }

    public String getName() {

        return name;
    }

    public Model getModel() {

        return model;
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
