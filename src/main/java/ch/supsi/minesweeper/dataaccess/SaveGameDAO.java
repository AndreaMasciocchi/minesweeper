package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.model.GridModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveGameDAO {
    private static final String userHomeDirectory = System.getProperty("user.home");
    private static final String savedGamesDirectory = ".minesweeper"+ File.separator+"savings";
    private static SaveGameDAO myself;
    private Path savingsPath;
    private File lastSavedFile = null;

    private SaveGameDAO(){
        try {
            savingsPath = Files.createDirectories(Path.of(userHomeDirectory,savedGamesDirectory));
        } catch (IOException e) {
            //
        }
    }

    public static SaveGameDAO getInstance(){
        if(myself == null){
            myself = new SaveGameDAO();
        }
        return myself;
    }

    public String getLastSavedFileAbsolutePath(){
        return lastSavedFile.getAbsolutePath();
    }

    public void persist(GridModel grid) throws FileNotFoundException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(lastSavedFile==null) {
            lastSavedFile = new File(savingsPath.toString() + File.separator + dateFormat.format(new Date())+".json");
            if(lastSavedFile.exists()) {
                File directory = new File(savingsPath.toString());
                File[] files = directory.listFiles();
                Arrays.sort(files);
                Pattern pattern = Pattern.compile("-([0-9]+)\\.");
                Matcher matcher;
                int previousNumber = 0;
                for(File file : files){
                    matcher = pattern.matcher(file.getName());
                    if(matcher.find())
                        previousNumber = Integer.parseInt(matcher.group(1))-previousNumber!=1 ? previousNumber : Integer.parseInt(matcher.group(1));
                }
                lastSavedFile = new File(savingsPath.toString() + File.separator + dateFormat.format(new Date()) +"-"+(++previousNumber)+".json");
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(grid);
        PrintWriter writer = new PrintWriter(lastSavedFile);
        writer.print(json);
        writer.close();
    }

    public void persist(GridModel grid, File file) throws FileNotFoundException{
        Gson gson = new Gson();
        String json = gson.toJson(grid);
        PrintWriter writer = new PrintWriter(file);
        writer.print(json);
        writer.close();
        lastSavedFile = file.getAbsoluteFile();
    }
}
