package io.deccan.worker.context;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class VariableResolverImpl
        implements VariableResolver {

    private static final Pattern
            VARIABLE_PATTERN =
            Pattern.compile("\\{\\{(.*?)}}");

    private final ExecutionContextHolder
            contextHolder;

    @Override
    public String resolve(
            String value){

        if(value==null){

            return null;

        }

        Matcher matcher =
                VARIABLE_PATTERN.matcher(value);

        StringBuffer buffer =
                new StringBuffer();

        while(matcher.find()){

            String variable =
                    matcher.group(1).trim();

            JsonNode node =
                    resolveNode(variable);

            String replacement =
                    node==null
                            ? ""
                            : node.isTextual()
                            ? node.asText()
                            : node.toString();

            matcher.appendReplacement(
                    buffer,
                    Matcher.quoteReplacement(
                            replacement));

        }

        matcher.appendTail(buffer);

        return buffer.toString();

    }

    private JsonNode resolveNode(
            String path){

        String[] tokens =
                path.split("\\.");

        JsonNode current =
                contextHolder.get()
                        .get(tokens[0]);

        if(current==null){

            return null;

        }

        for(int i=1;i<tokens.length;i++){

            current =
                    current.get(tokens[i]);

            if(current==null){

                return null;

            }

        }

        return current;

    }

}