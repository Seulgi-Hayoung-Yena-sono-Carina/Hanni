package Eom.blogtest.config.oauth;

import Eom.blogtest.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

public class OAuth2AuthorizationRequestBasedOnCookieRepository implements //Ctrl+O로 override할 메서드 선택
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME= "oauth2_auth_request";
    public static final int COOKIE_EXPIRE_SECONDS = 18000;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        //WebUtils.getCookie(request,OAUTH_2_AUTH_REQUEST); 문자열에 커서 올리고 Ctrl+Alt+C
        Cookie cookie = WebUtils.getCookie(request, OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest==null){
            removeAuthorizationRequestCookies(request,response);
            return;
        }

        CookieUtil.addCookie(response,OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest),COOKIE_EXPIRE_SECONDS);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request,response,OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

}