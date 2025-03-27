package com.splatref.splatrefbackend.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserFacingResponse {
    private String accessToken;
    private String handle;
    private String email;
}
