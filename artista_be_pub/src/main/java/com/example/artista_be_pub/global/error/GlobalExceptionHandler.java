package com.artista.main.global.error;

import com.artista.main.domain.constants.ResponseCode;
import com.artista.main.global.util.ReplaceStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.util.Objects;

import static com.artista.main.domain.constants.ResponseCode.INVALID_PARAMETER;
import static com.artista.main.domain.constants.ResponseCode.SERVER_ERROR;
import static com.artista.main.domain.constants.StaticValues.RESULT_CODE;
import static com.artista.main.domain.constants.StaticValues.RESULT_MESSAGE;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 기본 에러 헤더 응답
     * @param responseCode
     * @param message
     * @return
     */
    private HttpHeaders setHeaders(String responseCode, String message){
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(RESULT_CODE, responseCode);
        headers.add(RESULT_MESSAGE, message);

        return headers;
    }

    /**
     * db에러
     * @param ex
     * @return
     */
    @ExceptionHandler({JpaSystemException.class, UnexpectedException.class})
    protected ResponseEntity<Object> dbErrorHandle(Exception ex){
        log.info("DB Error", ex);

        ResponseCode resultCode = SERVER_ERROR;
        HttpHeaders headers = setHeaders(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());

        ApiResponse response = new ApiResponse(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());

        return new ResponseEntity<>(response, headers, resultCode.getHttpStatus());
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception ex) {
        ResponseCode resultCode = SERVER_ERROR;
        HttpHeaders headers = setHeaders(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());

        log.error("Exception", ex);

        ApiResponse response = new ApiResponse(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());
        return new ResponseEntity<>(response, headers, resultCode.getHttpStatus());
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(final CustomException ce){
        String encodeResultMessage = URLEncoder.encode(ce.getMessage(), StandardCharsets.UTF_8);
        HttpHeaders headers = setHeaders(ce.getResultCode(), encodeResultMessage);


        log.error("CustomException Code ={}, Message={}, HttpStatus={} Exception={}",
                ReplaceStringUtil.replaceStringCRLF(ce.getResultCode()),
                ReplaceStringUtil.replaceStringCRLF(ce.getResultMessage()),
                ReplaceStringUtil.replaceStringCRLF(Objects.toString(ce.getHttpStatus())),
                ReplaceStringUtil.replaceStringCRLF(ExceptionUtils.getStackTrace(ce)));

        ApiResponse response = new ApiResponse(ce.getResultCode(), encodeResultMessage, ce.getBody());

        return new ResponseEntity<>(response ,headers, ce.getHttpStatus());
    }

    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> ValidHandle(Exception ex) {
        log.error("ValidationException : ", ex);
        if (ex.getCause() instanceof CustomException) {
            return handleCustomException((CustomException) ex.getCause());
        }

        ResponseCode resultCode = INVALID_PARAMETER;

        HttpHeaders headers = setHeaders(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());
        ApiResponse response = new ApiResponse(resultCode.getResponseCode(), resultCode.getUrlEncodingMessage());
        return new ResponseEntity<>(response, headers, resultCode.getHttpStatus());
    }
}
