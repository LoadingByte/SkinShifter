
package de.unratedfilms.skinshifter;

import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Consts {

    public static final String MOD_ID       = "skinshifter";
    public static final String MOD_NAME     = "SkinShifter";
    public static final String MOD_VERSION  = "@MOD_VERSION@";               // Replaced during build

    public static final Logger LOGGER       = LogManager.getLogger(MOD_NAME);

    public static final String ROOT_PACKAGE = "de.unratedfilms." + MOD_ID;

    public static Path         MINECRAFT_DIR;                                // dynamic "constant"

    private Consts() {}

}
