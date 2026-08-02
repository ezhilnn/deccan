package io.deccan.controlplane.execution.node.mapper;

import io.deccan.controlplane.execution.node.dto.response.NodeExecutionResponse;
import io.deccan.controlplane.execution.node.entity.NodeExecution;
import org.springframework.stereotype.Component;

@Component
public class NodeExecutionMapper {

    public NodeExecutionResponse toResponse(
            NodeExecution entity) {

        return NodeExecutionResponse.builder()
                .id(entity.getId())
                .nodeId(entity.getNodeId())
                .nodeType(entity.getNodeType())
                .status(entity.getStatus())
                .startedAt(entity.getStartedAt())
                .finishedAt(entity.getFinishedAt())
                .durationMs(entity.getDurationMs())
                .output(entity.getOutput())
                .errorMessage(entity.getErrorMessage())
                .build();

    }

}