package ch.supsi.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.victools.jsonschema.generator.*;

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
        return new SchemaGenerator(getConfiguration()).generateSchema(clazz);
    }
    public boolean isJsonValid(JsonNode instance, JsonNode schema){
        try {
            return JsonSchemaFactory.byDefault().getJsonSchema(schema).validInstance(instance);
        } catch (ProcessingException e) {
            return false;
        }
    }
}
