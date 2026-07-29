package io.deccan.worker.context;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariableResolverImpl
        implements VariableResolver {

    private final ExecutionContextHolder
            contextHolder;

    @Override
    public String resolve(
            String value){

        if(value==null){
            return null;
        }

        if(!value.startsWith("{{")
                || !value.endsWith("}}")){

            return value;

        }

        String key =
                value.substring(
                        2,
                        value.length()-2)
                        .trim();

        JsonNode node =
                contextHolder
                        .get()
                        .get(key);

        if(node==null){
            return null;
        }

        if(node.isTextual()){
            return node.asText();
        }

        return node.toString();

    }

}