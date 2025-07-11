package com.example.authenticationservice.utill;

import com.example.authenticationservice.entity.AuthenUser;
import com.example.authenticationservice.exception.AppException;
import com.example.authenticationservice.exception.ErrorCode;
import com.example.authenticationservice.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refresh-valid-duration}")
    private long REFRESH_VALID_DURATION;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    public String generateToken(AuthenUser user) {
        try {
            // 1. Header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // 2. Claims
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserName())
                    .issuer("HT")
                    .issueTime(Date.from(Instant.now()))
                    .expirationTime(Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("userId", user.getId())
                    .claim("username", user.getUserName())
                    .claim("authorities", List.of("ROLE_" + user.getRole()))
                    .build();

            // 3. Sign the token
            JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(secretKey.getBytes()));

            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }


    public boolean validateToken(String token) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return signedJWT.verify(jwsVerifier)
                && expirationTime.after(new Date())
                && !invalidatedTokenRepository.existsById(
                signedJWT.getJWTClaimsSet().getJWTID());
    }

    public SignedJWT verifyToken(String token, Boolean isRefresh) throws JOSEException {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expirationTime = (isRefresh)
                    ? new Date(signedJWT
                    .getJWTClaimsSet()
                    .getExpirationTime()
                    .toInstant()
                    .plus(REFRESH_VALID_DURATION, ChronoUnit.SECONDS)
                    .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            boolean isSignatureValid = signedJWT.verify(jwsVerifier);
            boolean isNotExpired = expirationTime.after(new Date());
            boolean isNotRevoked = !invalidatedTokenRepository.existsById(jti);

            if (!(isSignatureValid && isNotExpired && isNotRevoked)) {
                throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
    }

}

