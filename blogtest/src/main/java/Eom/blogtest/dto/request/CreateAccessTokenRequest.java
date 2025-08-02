package Eom.blogtest.dto.request;

import lombok.Data;

@Data
public class CreateAccessTokenRequest {
    private String refreshToken;
}