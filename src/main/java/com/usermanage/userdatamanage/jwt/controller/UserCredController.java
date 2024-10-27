package com.usermanage.userdatamanage.jwt.controller;

import com.usermanage.userdatamanage.jwt.model.Authreq;
import com.usermanage.userdatamanage.jwt.model.RefreshToken;
import com.usermanage.userdatamanage.jwt.service.JwtService;
import com.usermanage.userdatamanage.jwt.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class UserCredController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates user credentials and returns JWT tokens"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateAndGenerateToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Authreq.class))
            )
            @RequestBody Authreq authreq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authreq.getUserName(), authreq.getPassWord())
        );

        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(authreq.getUserName());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authreq.getUserName());

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));
        }

        return ResponseEntity.status(401).build();
    }

    @Operation(
            summary = "Refresh token",
            description = "Get new access token using refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
            @ApiResponse(responseCode = "403", description = "Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserDetail)
                .map(userDetail -> {
                    String accessToken = jwtService.generateToken(userDetail.getUserName());
                    return ResponseEntity.ok(new AuthResponse(accessToken, request.getRefreshToken()));
                })
                .orElseThrow(() -> new RuntimeException ("Refresh token not found!"));
    }

    @Operation(
            summary = "Logout user",
            description = "Logout user and revoke refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        refreshTokenService.revokeRefreshToken(request.getUserName());
        return ResponseEntity.ok("Logged out successfully");
    }
}



@Schema(description = "Authentication response with tokens")
@Data
class AuthResponse {
    @Schema(description = "JWT access token")
    private String accessToken;

    @Schema(description = "Refresh token")
    private String refreshToken;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

@Schema(description = "Token refresh request")
@Data
class TokenRefreshRequest {
    @Schema(description = "Refresh token")
    private String refreshToken;
}

@Schema(description = "Logout request")
@Data
class LogoutRequest {
    @Schema(description = "Username to logout")
    private String userName;
}