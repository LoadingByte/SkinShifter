
package de.unratedfilms.skinshifter.common.skin.services;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class SkinRecorderService {

    // The skins that are currently worn by the players with the given names
    private static Map<String, Skin> recordedSkins = new HashMap<>();

    public static Skin getRecordedSkinOf(String playerName) {

        Validate.notBlank("Cannot retrieve the recorded skin of a player with a blank name");
        return recordedSkins.get(playerName);
    }

    public static void recordSkinChange(String playerName, Skin skin) {

        Validate.notBlank("Cannot record the skin of a player with a blank name");

        if (skin != null) {
            recordedSkins.put(playerName, skin);
        } else {
            recordedSkins.remove(playerName);
        }
    }

    private SkinRecorderService() {}

}
