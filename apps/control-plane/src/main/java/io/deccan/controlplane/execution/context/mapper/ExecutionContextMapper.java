package io.deccan.controlplane.execution.context.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.context.dto.response.ExecutionContextResponse;
import io.deccan.controlplane.execution.context.entity.ExecutionContextEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExecutionContextMapper {

    public ExecutionContextResponse toResponse(

            List<ExecutionContextEntity> entities){

        Map<String, JsonNode> outputs =
                new HashMap<>();

        for(ExecutionContextEntity entity : entities){

            outputs.put(

                    entity.getNodeId(),

                    entity.getOutput()

            );

        }

        return ExecutionContextResponse.builder()

                .nodeOutputs(outputs)

                .build();

    }

}