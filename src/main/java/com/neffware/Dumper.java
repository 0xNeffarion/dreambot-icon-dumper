package com.neffware;

import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.items.Item;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Dumper {

    private static final int DEBUG_STEP_SIZE = 2000;

    private static final int START_ID_RANGE = 1;
    private static final int END_ID_RANGE = 50_000;

    private static final String IMAGE_FORMAT = "png";
    private final File outputFolder;

    public Dumper(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    public void execute() {
        Logger.log("Output directory: " + outputFolder.getAbsolutePath());
        if (!resetDirectory(outputFolder)) {
            Logger.log("Unable to reset output directory");
            return;
        }

        Instant start = Instant.now();
        Logger.log("Starting icons dump now...");
        int count = dump(outputFolder);
        Duration duration = Duration.between(start, Instant.now()).abs();
        Logger.log("Finished icons dump!");
        Logger.log("Total count: " + count);
        Logger.log("Time: " + duration.getSeconds() + " seconds");
    }

    private int dump(File directory) {
        final AtomicInteger count = new AtomicInteger(0);
        IntStream.range(START_ID_RANGE, END_ID_RANGE)
                .parallel()
                .forEach(id -> getItemIcon(id).ifPresent(icon -> {
                    if (saveIcon(icon, id, directory)) {
                        int current = count.incrementAndGet();

                        if (current % DEBUG_STEP_SIZE == 0) {
                            Logger.log("#" + current);
                        }
                    }
                }));

        return count.get();
    }

    private boolean resetDirectory(File directory) {
        try {
            if (!directory.exists()) {
                Files.createDirectories(directory.toPath());
            }
        } catch (IOException e) {
            return false;
        }

        File[] files = directory.listFiles();
        if (files != null && files.length == 0) {
            return true;
        }

        Logger.log("Deleting previous files...");
        try (Stream<Path> stream = Files.walk(directory.toPath())) {
            stream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);

            Logger.log("Files deleted!");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }

            return true;
        } catch (IOException e) {
            Logger.log("Failed to clean directory: " + e);
        }

        return false;
    }

    private Optional<BufferedImage> getItemIcon(int id) {
        try {
            Item item = new Item(id, 1);
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                BufferedImage icon = item.getImage();
                if (icon != null) {
                    return Optional.of(icon);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        } catch (Exception e) {
            Logger.log("Failed to get icon of item #" + id);
        }

        return Optional.empty();
    }

    private boolean saveIcon(BufferedImage icon, int id, File directory) {
        try {
            String fileName = String.format("%d.%s", id, IMAGE_FORMAT);
            File outputFile = directory.toPath().resolve(fileName).toFile();
            return ImageIO.write(icon, IMAGE_FORMAT, outputFile);

        } catch (IOException ex) {
            Logger.log("Failed to save icon: " + directory.getName());
        }

        return false;
    }

}
