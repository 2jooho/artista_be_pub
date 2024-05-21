package com.artista.main.global.error;

import com.artista.main.domain.constants.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.artista.main.domain.constants.ResponseCode.SERVER_ERROR;

public class CustomException extends RuntimeException {
    @Getter private final String resultCode;
    @Getter private final String resultMessage;
    @Getter private final HttpStatus httpStatus;
    @Getter private final transient Object body;

    public CustomException() {
        this(SERVER_ERROR);
    }

    public CustomException(ResponseCode responseCode) {
        super("[" + responseCode.getResponseCode() + "]"+responseCode.getMessage());
        this.resultCode = responseCode.getResponseCode();
        this.resultMessage = responseCode.getMessage();
        this.body = null;
        this.httpStatus = responseCode.getHttpStatus();
    }

    public CustomException(ResponseCode responseCode, String resultMessage) {
        super("[" + responseCode.getResponseCode() + "]"+responseCode.getMessage());
        this.resultCode = responseCode.getResponseCode();
        this.resultMessage = resultMessage;
        this.body = null;
        this.httpStatus = responseCode.getHttpStatus();
    }
    public CustomException(String resultCode, String resultMessage, Object responseBody) {
        super("[" + resultCode + "] " + resultMessage);
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.body = responseBody;
        this.httpStatus = ResponseCode.getHttpStatusFromResponseCode(resultCode);
    }

}
