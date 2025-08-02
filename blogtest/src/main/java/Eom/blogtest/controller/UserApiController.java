package Eom.blogtest.controller;

import Eom.blogtest.config.jwt.TokenProvider;
import Eom.blogtest.dto.request.AddUserRequest;
import Eom.blogtest.repository.RefreshTokenRepository;
import Eom.blogtest.service.UserService;
import Eom.blogtest.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RequiredArgsConstructor
//@Controller
//public class UserApiController {
//    private final UserService userService;
//    @PostMapping("/user")
//    public String signup(AddUserRequest request) {
//        userService.save(request);
//        return "redirect:/login";
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response,
//                SecurityContextHolder.getContext().getAuthentication());
//        return "redirect:/login";
//    }
//}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token"; //쿠키에서 사용할 토큰 이름 상수로 정의

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @DeleteMapping("/refresh-token")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        CookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME)
                .map(Cookie::getValue) //요청에서 "refresh_token" 쿠키를 꺼내고
                .ifPresent(refreshToken -> { //값이 있으면

                    if (tokenProvider.validToken(refreshToken)) {
                        Long userId = tokenProvider.getUserId(refreshToken);
                        refreshTokenRepository.findByUserId(userId)
                                .ifPresent(refreshTokenRepository::delete);
                    }
                });


        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok().build(); // 상태 코드 200
    }
}