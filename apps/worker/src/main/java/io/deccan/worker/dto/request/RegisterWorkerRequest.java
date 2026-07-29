package io.deccan.worker.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWorkerRequest {

    private String workerName;

    private String hostName;

    private JsonNode capabilities;

}