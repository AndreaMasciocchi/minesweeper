package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.Exceptions.FileProcessingException;
import ch.supsi.minesweeper.Exceptions.FileSyntaxException;
import ch.supsi.minesweeper.Exceptions.MalformedFileException;
import ch.supsi.minesweeper.model.Cell;
import ch.supsi.minesweeper.model.GridModel;
import ch.supsi.minesweeper.model.JsonValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonPersistenceDAO extends PersistenceDAO {
    private static JsonPersistenceDAO myself;
    private File lastSavedFile = null;
    private Gson serializer = null;
    private Gson deserializer = null;
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
        Gson gson = getSerializer();
        String json = gson.toJson(o);
        PrintWriter writer = new PrintWriter(lastSavedFile);
        writer.print(json);
        writer.close();
    }
    @Override
    public void persist(Object o, File file) throws FileNotFoundException{
        Gson gson = getSerializer();
        String json = gson.toJson(o);
        PrintWriter writer = new PrintWriter(file);
        writer.print(json);
        writer.close();
        lastSavedFile = file.getAbsoluteFile();
    }
    @Override
    public Object deserialize(File file, Class<?> clazz) throws MalformedFileException, FileProcessingException, FileSyntaxException, FileNotFoundException {
        Scanner reader = new Scanner(file);
        String json;
        StringBuilder sb = new StringBuilder();
        while(reader.hasNextLine())
            sb.append(reader.nextLine());
        json = sb.toString();
        reader.close();
        try{
            new JSONObject(json);
        }catch (JSONException e){
            throw new MalformedFileException();
        }
        JsonValidator validator = new JsonValidator();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new FileProcessingException();
        }
        if(!validator.isJsonValid(jsonNode,validator.getJsonSchema(clazz)))
            throw new MalformedFileException();
        Gson gson = getDeserializer();
        Object o;
        try{
            o = gson.fromJson(json,clazz);
        }catch (JsonParseException e){
            throw new FileSyntaxException();
        }
        return o;
    }

    private Gson getSerializer(){
        if(serializer ==null){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Cell.class,new CellAdapter());
            serializer = builder.create();
        }
        return serializer;
    }

    private Gson getDeserializer(){
        if(deserializer==null){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(GridModel.class,(InstanceCreator<GridModel>) type -> GridModel.getInstance());
            builder.registerTypeAdapter(Cell.class,new CellAdapter());
            deserializer = builder.create();
        }
        return deserializer;
    }
}
