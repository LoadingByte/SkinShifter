
package de.unratedfilms.skinshifter.common.skin.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.skinshifter.common.skin.Skin;

/**
 * This class is only used by the server in order to record which player chose which custom skin.
 * That information is needed if new players join and want to know who wears what skin.
 * Note that this class is fully thread-safe.
 */
public class SkinRecorderService {

    // The skins that are currently worn by the players with the given names
    private static Map<String, Skin> recordedSkins = new ConcurrentHashMap<>();

    public static Optional<Skin> getRecordedSkinOf(String playerName) {

        Validate.notBlank(playerName, "Cannot retrieve the recorded skin of a player with a blank name");

        return Optional.ofNullable(recordedSkins.get(playerName));
    }

    public static void recordSkinSet(String playerName, Skin skin) {

        Validate.notBlank(playerName, "Cannot record the skin of a player with a blank name");
        Validate.notNull(skin, "Cannot record a null skin");

        recordedSkins.put(playerName, skin);
    }

    public static void recordSkinClear(String playerName) {

        Validate.notBlank(playerName, "Cannot record the skin clear action for a player with a blank name");

        recordedSkins.remove(playerName);
    }

    private SkinRecorderService() {}

}
