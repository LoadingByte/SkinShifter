
package de.unratedfilms.skinshifter.client.keys;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import de.unratedfilms.skinshifter.Consts;

public class KeyBindings {

    public static KeyBinding openSkinSelectionScreen = createKeyBinding("openSkinSelectionScreen", Keyboard.KEY_O);

    private static KeyBinding createKeyBinding(String name, int key) {

        return new KeyBinding("key." + Consts.MOD_ID + "." + name, key, "key.categories." + Consts.MOD_ID);
    }

    public static void initialize() {

        ClientRegistry.registerKeyBinding(openSkinSelectionScreen);
    }

}
