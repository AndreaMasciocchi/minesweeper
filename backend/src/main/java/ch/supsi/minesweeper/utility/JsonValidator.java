package ch.supsi.minesweeper.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.victools.jsonschema.generator.*;

import java.io.IOException;
import java.io.InputStream;

public class JsonValidator {
    private final SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    private SchemaGeneratorConfig getConfiguration(){
        configBuilder.forFields()
                .withRequiredCheck(field -> {
                    JsonProperty jsonProperty = field.getAnnotationConsideringFieldAndGetter(JsonProperty.class) ;
                    if ( jsonProperty == null )
                        return false;
                    else
                        return jsonProperty.required();
                });
        return configBuilder.build();
    }
    public JsonNode getJsonSchema(Class<?> clazz){
        JsonNode schema = null;
        try(InputStream ioStream = this.getClass().getResourceAsStream("/JsonSchemas/"+clazz.getSimpleName()+".json")){
            if(ioStream==null){
                return new SchemaGenerator(getConfiguration()).generateSchema(clazz);
            }
            ObjectMapper mapper = new ObjectMapper();
            schema = mapper.readTree(ioStream);
        }catch (IOException e){
            System.out.println("An error occurred while reading the schema");
        }
        return schema;
    }
    public boolean isJsonValid(JsonNode instance, JsonNode schema){
        try {
            return JsonSchemaFactory.byDefault().getJsonSchema(schema).validInstance(instance);
        } catch (ProcessingException e) {
            return false;
        }
    }
}
