
package de.unratedfilms.skinshifter;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String[] skinDirectories;

    public static void initialize(File configFile) {

        Configuration config = new Configuration(configFile);

        syncConfig(config);

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void syncConfig(Configuration config) {

        skinDirectories = config.getStringList("directories", "skins", new String[] { "mods/" + Consts.MOD_ID + "/skins" },
                "The directories which are recursively searched for skins; they are created if they don't exist yet.");
    }

}
