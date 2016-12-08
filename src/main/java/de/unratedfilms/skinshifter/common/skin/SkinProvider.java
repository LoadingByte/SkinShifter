
package de.unratedfilms.skinshifter.common.skin;

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
import de.unratedfilms.skinshifter.Config;
import de.unratedfilms.skinshifter.Consts;

public class SkinProvider {

    public static Set<Skin> getAvailableSkins() {

        Set<Skin> skins = new HashSet<>();

        for (String directoryPath : Config.skinDirectories) {
            final Path directory = Consts.MINECRAFT_DIR.resolve(directoryPath);

            // Create the skin directory if it doesn't exist yet
            if (!Files.exists(directory)) {
                LOGGER.info("Creating new configured skin dir under '{}'", directory);

                try {
                    Files.createDirectories(directory);
                } catch (IOException e) {
                    LOGGER.error("Failed to create the configured skin dir '{}'", directory, e);
                }
            }

            LOGGER.info("Checking dir '{}' for potential skins", directory);

            try {
                // Recursively walk through the file tree and find all skin files
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                        try {
                            // Get the skin name
                            String skinName = FilenameUtils.removeExtension(directory.relativize(file).toString());
                            // Load the skin into a bufferedimage
                            BufferedImage skinTexture = ImageIO.read(file.toFile());

                            skins.add(new Skin(skinName, skinTexture));
                        } catch (IOException e) {
                            LOGGER.error("Error while reading custom skin file '{}'", file, e);
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

    private SkinProvider() {}

}
