package com.artista.main.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    INVALID_RESPONSE("9999", "요청이 처리 되지 않았습니다.",HttpStatus.INTERNAL_SERVER_ERROR),

    /** 공통 */
    SUCCESS("0000", "정상 처리되었습니다.",HttpStatus.OK),
    SERVER_ERROR("0099", "서비스 접속이 원활하지 않습니다. 잠시 후 다시 이용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_PARAMETER("0003", "요청 파라미터의 값이 잘못되었습니다.",HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("0005", "잘못된 요청입니다.",HttpStatus.BAD_REQUEST),
    NO_DATA("0009", "조회된 데이터가 없습니다.",HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH("101","비밀번호 불일치", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_EXISTS_USER("102", "이미 계정이 존재합니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("103", "존재하지 않는 계정", HttpStatus.NOT_FOUND),
    VALIDATION_FAIL("104", "값이 유효하지 않음", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("105", "잘못된 접근", HttpStatus.INTERNAL_SERVER_ERROR),
    RE_TOKEN_RESPONSE("113", "토큰 재발급", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_COUNT_FAIL("114", "로그인 횟수 초과", HttpStatus.INTERNAL_SERVER_ERROR),
    STAR_IMG_NOT_FOUND("115", "스타 이미지 정보 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    RESET_PASSWORD_MATCH_FAIL("116", "비밀번호와 비밀번호 확인이 다릅니다.", HttpStatus.BAD_REQUEST),
    CATE_LIST_NOT_FOUND("117", "카테고리 정보 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    COLLECTION_INFO_FOUND("118", "컬렉션 정보 존재", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_EXISTS_NICKNAME("119", "이미 닉네임이 존재합니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String responseCode;
    private final String message;
    private final HttpStatus httpStatus;

    public String getUrlEncodingMessage(){
        return URLEncoder.encode(this.message, StandardCharsets.UTF_8);
    }

    //Map -> ImmutableMap 바꿔도 된다.
    private static final Map<String, ResponseCode> codes = Map.copyOf(
            Stream.of(values()).collect(Collectors.toMap(ResponseCode::getResponseCode, Function.identity())));

    public static HttpStatus getHttpStatusFromResponseCode(String responseCode){
        if(codes.get(responseCode)!=null)
            return codes.get(responseCode).getHttpStatus();
        else
            return HttpStatus.INTERNAL_SERVER_ERROR;
    }


}
