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
import java.util.Date;

public class SaveGameDAO {
    private static final String userHomeDirectory = System.getProperty("user.home");
    private static final String savedGamesDirectory = ".minesweeper"+ File.separator+"savings";
    private static SaveGameDAO myself;
    private int sequentialFileNumber = 1;
    private Path savingsPath;

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

    public void persist(GridModel grid) throws FileNotFoundException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(savingsPath.toString()+File.separator+dateFormat.format(new Date()));
        if(file.exists())
            file = new File(savingsPath.toString()+File.separator+dateFormat.format(new Date())+"-"+(sequentialFileNumber++));
        else
            sequentialFileNumber = 1;
        Gson gson = new Gson();
        String json = gson.toJson(grid);
        PrintWriter writer = new PrintWriter(file);
        writer.println(json);
        writer.close();
        System.out.println("Game saved to "+file.getName());
    }

    public void persist(GridModel grid, File file) throws FileNotFoundException{
        Gson gson = new Gson();
        String json = gson.toJson(grid);
        PrintWriter writer = new PrintWriter(file);
        writer.println(json);
        writer.close();
        System.out.println("Game saved to "+file.getName());
    }
}
