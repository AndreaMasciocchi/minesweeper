package ch.supsi.minesweeper.dataaccess;

import ch.supsi.minesweeper.model.Cell;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CellAdapter implements JsonSerializer<Cell>, JsonDeserializer<Cell> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String DATA = "DATA";

    public Cell deserialize(JsonElement jsonElement, Type type,
                           JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        if(prim==null)
            throw new JsonParseException("");
        String className = prim.getAsString();
        Class klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("");
        }
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
    }

    public JsonElement serialize(Cell jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
        return jsonObject;
    }
}
