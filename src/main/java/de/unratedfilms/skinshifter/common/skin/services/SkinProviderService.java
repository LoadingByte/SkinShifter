
package de.unratedfilms.skinshifter.common.skin.services;

import static de.unratedfilms.skinshifter.Consts.LOGGER;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import de.unratedfilms.skinshifter.Config;
import de.unratedfilms.skinshifter.Consts;
import de.unratedfilms.skinshifter.common.skin.Skin;

public class SkinProviderService {

    public static Set<Skin> getAvailableSkins() {

        Set<Skin> skins = new HashSet<>();

        for (Path directory : getAllSkinDirectories()) {
            LOGGER.info("Checking dir '{}' for potential skins", directory);

            try {
                // Recursively walk through the file tree and find all skin files
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                        if (FilenameUtils.getExtension(file.toString()).equalsIgnoreCase("png")) {
                            try {
                                // Get the skin name
                                String skinName = FilenameUtils.removeExtension(directory.relativize(file).toString());
                                // Load the skin into a bufferedimage
                                BufferedImage skinTexture = ImageIO.read(file.toFile());

                                skins.add(new Skin(skinName, skinTexture));
                            } catch (IOException e) {
                                LOGGER.error("Error while reading custom skin file '{}'", file, e);
                            }
                        }

                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

                        LOGGER.error("Failed to visit the file '{}' while searching for potential skin files", file, exc);
                        return FileVisitResult.CONTINUE;
                    }

                });
            } catch (IOException e) {
                LOGGER.error("Unexpected exception while iterating through the skin file dir '{}'", directory, e);
            }
        }

        return skins;
    }

    private static Set<Path> getAllSkinDirectories() {

        Set<Path> skinDirs = new HashSet<>();

        // Add the configured skin directories
        for (String skinDirPath : Config.skinDirectories) {
            Path skinDir = Consts.MINECRAFT_DIR.resolve(skinDirPath);
            skinDirs.add(skinDir);

            // Create the skin directory if it doesn't exist yet
            if (!Files.exists(skinDir)) {
                LOGGER.info("Creating new configured skin dir under '{}'", skinDir);

                try {
                    Files.createDirectories(skinDir);
                } catch (IOException e) {
                    LOGGER.error("Failed to create the configured skin dir '{}'", skinDir, e);
                }
            }
        }

        // Add a skin directory for each world
        for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
            Path skinDir = world.getSaveHandler().getWorldDirectory().toPath().resolve("skins");
            if (Files.exists(skinDir)) {
                skinDirs.add(skinDir);
            }
        }

        return skinDirs;
    }

    private SkinProviderService() {}

}
