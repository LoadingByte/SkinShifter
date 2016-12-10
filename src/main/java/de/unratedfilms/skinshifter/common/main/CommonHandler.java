
package de.unratedfilms.skinshifter.common.main;

import java.nio.file.Paths;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.unratedfilms.skinshifter.Config;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.common.event.RemoveAbandonedSkinsEventHandler;
import de.unratedfilms.skinshifter.net.NetworkService;

public class CommonHandler {

    public void preInit(FMLPreInitializationEvent event) {

        // Retrieve dynamic "constants"
        Consts.MINECRAFT_DIR = Paths.get(".");

        // Initialize the configuration
        Config.initialize(event.getSuggestedConfigurationFile());

        // Initialize the network service
        NetworkService.initialize();
    }

    public void init(FMLInitializationEvent event) {

        // Initialize the skin record cleanup mechanism
        FMLCommonHandler.instance().bus().register(new RemoveAbandonedSkinsEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}
