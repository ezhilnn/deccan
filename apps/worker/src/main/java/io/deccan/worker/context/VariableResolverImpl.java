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
    @Override
public boolean evaluate(
        String expression){

    if(expression == null){

        return false;

    }

    expression = expression.trim();

    expression = resolve(
            expression);

    if(expression.contains("==")){

        String[] p =
                expression.split("==",2);

        return p[0].trim()
                .equals(
                        p[1].trim());

    }

    if(expression.contains("!=")){

        String[] p =
                expression.split("!=",2);

        return !p[0].trim()
                .equals(
                        p[1].trim());

    }

    if(expression.contains(">=")){

        String[] p =
                expression.split(">=",2);

        return Double.parseDouble(
                p[0].trim())
                >=
                Double.parseDouble(
                        p[1].trim());

    }

    if(expression.contains("<=")){

        String[] p =
                expression.split("<=",2);

        return Double.parseDouble(
                p[0].trim())
                <=
                Double.parseDouble(
                        p[1].trim());

    }

    if(expression.contains(">")){

        String[] p =
                expression.split(">",2);

        return Double.parseDouble(
                p[0].trim())
                >
                Double.parseDouble(
                        p[1].trim());

    }

    if(expression.contains("<")){

        String[] p =
                expression.split("<",2);

        return Double.parseDouble(
                p[0].trim())
                <
                Double.parseDouble(
                        p[1].trim());

    }

    return Boolean.parseBoolean(
            expression);

}
}