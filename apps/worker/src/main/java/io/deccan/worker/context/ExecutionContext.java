package io.deccan.worker.context;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ExecutionContext {

    private final Map<String, JsonNode> variables =
            new HashMap<>();

    public void put(
            String key,
            JsonNode value){

        variables.put(
                key,
                value);

    }

    public JsonNode get(
            String key){

        return variables.get(
                key);

    }

}