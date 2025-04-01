package ch.supsi.minesweeper.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class ReadWriteUtilities {
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static Properties read(File file) {
        return null;
    }

    public static void write(File file) {

    }

    public static void create() {
        File dir = new File(System.getProperty("user.home")+"\\minesweeper");

        if (!dir.exists()) {
            dir.mkdir();
        }
/*

        try {
            Files.copy(saves.toPath(), dir.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(settings.toPath(), dir.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
*/
    }
}
