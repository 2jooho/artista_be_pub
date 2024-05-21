package com.artista.main.global.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApiResponse {
    private final String resultCode;
    private final String resultMessage;
    private final Object resultBody;

    public ApiResponse(String resultCode, String resultMessage) {
        this(resultCode, resultMessage, null);
    }

    @JsonCreator
    public ApiResponse(@JsonProperty("resultCode")String resultCode
            , @JsonProperty("resultMessage")String resultMessage, @JsonProperty("resultBody")Object resultBody) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.resultBody = resultBody;
    }

}
