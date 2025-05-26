package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.model.DataPersistenceInterface;
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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonPersistenceDAO extends PersistenceDAO{
    private static JsonPersistenceDAO myself;
    private File lastSavedFile = null;
    private JsonPersistenceDAO(){
        super();
    }
    public static JsonPersistenceDAO getInstance(){
        if(myself == null){
            myself = new JsonPersistenceDAO();
        }
        return myself;
    }
    @Override
    public String getLastSavedFileAbsolutePath(){
        return lastSavedFile.getAbsolutePath();
    }

    @Override
    public void persist(Object o) throws FileNotFoundException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(lastSavedFile==null) {
            lastSavedFile = new File(savingsPath.toString() + File.separator + dateFormat.format(new Date())+".json");
            if(lastSavedFile.exists()) {
                File directory = new File(savingsPath.toString());
                File[] files = directory.listFiles();
                Arrays.sort(files);
                Pattern pattern = Pattern.compile("("+dateFormat.format(new Date())+")"+"-([0-9]+)\\.");
                Matcher matcher;
                int previousNumber = 0;
                for(File file : files){
                    matcher = pattern.matcher(file.getName());
                    if(matcher.find())
                        previousNumber = Integer.parseInt(matcher.group(2))-previousNumber!=1 ? previousNumber : Integer.parseInt(matcher.group(2));
                }
                lastSavedFile = new File(savingsPath.toString() + File.separator + dateFormat.format(new Date()) +"-"+(++previousNumber)+".json");
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(o);
        PrintWriter writer = new PrintWriter(lastSavedFile);
        writer.print(json);
        writer.close();
    }
    @Override
    public void persist(Object o, File file) throws FileNotFoundException{
        Gson gson = new Gson();
        String json = gson.toJson(o);
        PrintWriter writer = new PrintWriter(file);
        writer.print(json);
        writer.close();
        lastSavedFile = file.getAbsoluteFile();
    }
}
