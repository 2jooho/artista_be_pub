package com.artista.main.domain.constants;

public class ApiUrl {
    private ApiUrl(){}

    public static final String BASE_URL = "/aa";
    public static final String LOGIN_URL = "/auth/login";
    public static final String JOIN_URL = "/auth/join";

    public static final String AUTH_TYPE_LOGIN_URL = "/auth/{socialLoginType}/login";
    public static final String AUTH_CALLBACK_URL = "/auth/{socialLoginType}/callback";
    public static final String AUTH_PASS_URL = "/auth/pass";
    public static final String AUTH_PASS_SUCCESS_URL = "/auth/pass/success";
    public static final String AUTH_PASS_SUCCESS_CALLBACK_URL = "/auth/pass/success/callback";
    public static final String AUTH_PASS_FAIL_URL = "/auth/pass/fail";
    public static final String MAIN_URL = "/main.do";
    public static final String GALLERY_UPLOAD_URL_TEST = "/gallery/upload/test";
    public static final String ID_CHECK_URL = "/user/idCheck";
    public static final String NICKNAME_CHECK_URL = "/user/nickNameCheck";
    public static final String USER_UPDATE_URL = "/user/update";
    public static final String FIND_ID_URL = "/user/findId";
    public static final String REST_PW_URL = "/user/restPw";
    public static final String REST_PW_CHECK_URL = "/user/restPw/Check";
    public static final String CATE_LIST_URL = "/gallery/categoryList";
    public static final String FIRST_INFO_URL = "/gallery/firstInfo";
    public static final String GALLERY_LIST_URL = "/gallery/galleryList";
    public static final String GALLERY_DETAIL_URL = "/gallery/galleryDetail";
    public static final String GALLERY_UPLOAD_URL = "/gallery/galleryUpload";
    public static final String GALLERY_UPDATE_URL = "/gallery/galleryUpdate";
    public static final String GALLERY_DELETE_URL = "/gallery/galleryDelete/{galleryId}";
    public static final String STAR_LIST_URL = "/star/starList";
    public static final String STAR_DATE_LIST_URL = "/star/starDateList";
    public static final String COLLABOR_LIST_URL = "/collabor/collaborList";
    public static final String COLLECTION_INFO_URL = "/collection/collectionInfo";
    public static final String COLLECTION_UPLOAD_URL = "/collection/upload";
    public static final String COLLECTION_UPDATE_URL = "/collection/update";
    public static final String LIKE_ART_LIST_URL = "/collection/likeArtList";
    public static final String COLLECTION_STAR_LIST_URL = "/collection/starList";
    public static final String SERCH_URL = "/serch";
    public static final String ART_LIKE_URL = "/gallery/likeUpdate";
}
