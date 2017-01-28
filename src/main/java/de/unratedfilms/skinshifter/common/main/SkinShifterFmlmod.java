
package de.unratedfilms.skinshifter.common.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import de.unratedfilms.skinshifter.Consts;

@Mod (modid = Consts.MOD_ID, version = Consts.MOD_VERSION)
public class SkinShifterFmlmod {

    @SidedProxy (clientSide = Consts.ROOT_PACKAGE + ".client.main.ClientHandler", serverSide = Consts.ROOT_PACKAGE + ".common.main.CommonHandler")
    private static CommonHandler proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }

}
