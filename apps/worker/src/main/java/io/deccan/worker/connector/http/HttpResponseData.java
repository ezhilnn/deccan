package io.deccan.worker.connector.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HttpResponseData {

    private int status;

    private JsonNode body;

}