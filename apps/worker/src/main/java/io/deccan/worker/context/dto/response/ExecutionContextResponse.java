package io.deccan.worker.context.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExecutionContextResponse {

    private Map<String, JsonNode> nodeOutputs;

}