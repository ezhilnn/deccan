package io.deccan.controlplane.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String generateToken(User user){

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",user.getId())
                .claim("organizationId",user.getOrganization().getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+properties.getExpiration()))
                .signWith(signingKey())
                .compact();

    }

    public String extractUsername(String token){

        return extractClaims(token).getSubject();

    }
    public boolean isValid(
        String token,
        UserDetails userDetails
    ) {

        return extractUsername(token)
                .equals(userDetails.getUsername())
                &&
                extractClaims(token)
                        .getExpiration()
                        .after(new Date());

    }

    private Claims extractClaims(String token){

        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey signingKey(){

        byte[] key= Decoders.BASE64.decode(properties.getSecret());

        return Keys.hmacShaKeyFor(key);

    }
   public UUID extractUserId(String token) {

    return UUID.fromString(

            extractClaims(token)
                    .get("userId")
                    .toString()

    );

    }

    public UUID extractOrganizationId(String token) {

        return UUID.fromString(

                extractClaims(token)
                        .get("organizationId")
                        .toString()

        );

    }

}