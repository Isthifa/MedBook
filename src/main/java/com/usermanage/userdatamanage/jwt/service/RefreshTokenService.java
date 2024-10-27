package com.usermanage.userdatamanage.jwt.service;

import com.usermanage.userdatamanage.entity.UserDetail;
import com.usermanage.userdatamanage.jwt.Repo.RefreshTokenRepo;
import com.usermanage.userdatamanage.jwt.model.RefreshToken;
import com.usermanage.userdatamanage.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")  // Add this to application.properties: jwt.refresh.expiration=604800000 (7 days)
    private Long refreshTokenDuration;

    @Autowired
    private RefreshTokenRepo refreshTokenRepository;

    @Autowired
    private UserDetailsRepo userDetailRepository;

    public RefreshToken createRefreshToken(String username) {
        UserDetail userDetail = userDetailRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already has a refresh token
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserDetail(userDetail);
        if (existingToken.isPresent()) {
            refreshTokenRepository.delete(existingToken.get());
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserDetail(userDetail);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0 || token.isRevoked()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void revokeRefreshToken(String username) {
        UserDetail userDetail = userDetailRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserDetail(userDetail);
        if (refreshToken.isPresent()) {
            RefreshToken token = refreshToken.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }
}
@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }
}
