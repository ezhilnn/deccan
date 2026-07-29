package io.deccan.worker.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class ConfigurationResolver
        implements JsonVariableResolver {

    private final VariableResolver variableResolver;

    @Override
    public JsonNode resolve(JsonNode node) {

        if (node == null) {
            return null;
        }

        if (node.isObject()) {

            ObjectNode object = (ObjectNode) node;

            Iterator<String> iterator = object.fieldNames();

            while (iterator.hasNext()) {

                String field = iterator.next();

                object.set(
                        field,
                        resolve(object.get(field))
                );
            }

            return object;
        }

        if (node.isArray()) {

            ArrayNode array = (ArrayNode) node;

            for (int i = 0; i < array.size(); i++) {

                array.set(
                        i,
                        resolve(array.get(i))
                );
            }

            return array;
        }

        if (node.isTextual()) {

            return TextNode.valueOf(
                    variableResolver.resolve(
                            node.asText()
                    )
            );
        }

        return node;
    }
}