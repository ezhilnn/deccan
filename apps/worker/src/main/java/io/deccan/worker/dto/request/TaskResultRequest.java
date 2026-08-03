package io.deccan.worker.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResultRequest {

    private boolean success;

    private String errorMessage;

    private JsonNode output;

}