
package de.unratedfilms.skinshifter.client.main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.client.event.PollSkinsEventHandler;
import de.unratedfilms.skinshifter.client.keys.KeyBindings;
import de.unratedfilms.skinshifter.client.keys.KeyHandler;
import de.unratedfilms.skinshifter.common.main.CommonHandler;

public class ClientHandler extends CommonHandler {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        super.preInit(event);

        // The minecraft dir might be different on the client
        Consts.MINECRAFT_DIR = Minecraft.getMinecraft().mcDataDir.toPath();
    }

    @Override
    public void init(FMLInitializationEvent event) {

        super.init(event);

        // Initialize the key bindings
        KeyBindings.initialize();
        FMLCommonHandler.instance().bus().register(new KeyHandler());

        // Initialize the skin polling mechanism
        MinecraftForge.EVENT_BUS.register(new PollSkinsEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

        super.postInit(event);
    }

}
