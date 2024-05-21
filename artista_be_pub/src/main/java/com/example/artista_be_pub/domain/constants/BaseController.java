package com.artista.main.domain.constants;

import org.springframework.http.HttpHeaders;

import static com.artista.main.domain.constants.ResponseCode.SUCCESS;
import static com.artista.main.domain.constants.StaticValues.RESULT_CODE;
import static com.artista.main.domain.constants.StaticValues.RESULT_MESSAGE;


public class BaseController {

    protected HttpHeaders getSuccessHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RESULT_CODE, SUCCESS.getResponseCode());
        headers.set(RESULT_MESSAGE, SUCCESS.getUrlEncodingMessage());
        return headers;
    }

}
