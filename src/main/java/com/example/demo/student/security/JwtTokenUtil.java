package com.example.demo.student.security;

import com.example.demo.student.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

   static final String CLAIM_KEY_USERNAME = "sub";
   static final String CLAIM_KEY_AUDIENCE = "aud";
   static final String CLAIM_KEY_CREATED = "iat";

   @Value("${jwt.secret}")
   private String secret;

   @Value("${jwt.expiration}")
   private Long expiration;
   @Value("${jwt.refreshTokenExpirationDate}")
   private Long refreshTokenExpirationDate;

   public String getUsernameFromToken(String token) {
       return getClaimFromToken(token, Claims::getSubject);
   }

   public Date getIssuedAtDateFromToken(String token) {
       return getClaimFromToken(token, Claims::getIssuedAt);
   }

   public Date getExpirationDateFromToken(String token) {
       return getClaimFromToken(token, Claims::getExpiration);
   }

   public String getAudienceFromToken(String token) {
       return getClaimFromToken(token, Claims::getAudience);
   }

   public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
       final Claims claims = getAllClaimsFromToken(token);
       return claimsResolver.apply(claims);
   }

   private Claims getAllClaimsFromToken(String token) {
       return Jwts.parser()
               .setSigningKey(secret)
               .parseClaimsJws(token)
               .getBody();
   }

   private Boolean isTokenExpired(String token) {
       final Date expiration = getExpirationDateFromToken(token);
       return expiration.before(new Date());
   }

   private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
       return (lastPasswordReset != null && created.before(lastPasswordReset));
   }


   public String generateToken(String email) {
       Map<String, Object> claims = new HashMap<>();
       return doGenerateToken(claims, email);
   }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getEmail());
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshTokenToken(claims, user.getEmail());
    }

   private String doGenerateToken(Map<String, Object> claims, String subject) {
       final Date createdDate = new Date();
       final Date expirationDate = calculateExpirationDate(createdDate, expiration);

       System.out.println("doGenerateToken " + createdDate);

       return Jwts.builder()
               .setClaims(claims)
               .setSubject(subject)
               .setIssuedAt(createdDate)
               .setExpiration(expirationDate)
               .signWith(SignatureAlgorithm.HS512, secret)
               .compact();
   }
    private String doGenerateRefreshTokenToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate, refreshTokenExpirationDate);

        System.out.println("doGenerateToken " + createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

//   public String refreshToken(String token) {
//       final Date createdDate = new Date();
//       final Date expirationDate = calculateExpirationDate(createdDate);
//
//       final Claims claims = doGenerateToken(token);
//       claims.setIssuedAt(createdDate);
//       claims.setExpiration(expirationDate);
//
//       return Jwts.builder()
//               .setClaims(claims)
//               .signWith(SignatureAlgorithm.HS512, secret)
//               .compact();
//   }

   public Boolean validateToken(String token, String email) {

       final String username = getUsernameFromToken(token);
       final Date created = getIssuedAtDateFromToken(token);
       //final Date expiration = getExpirationDateFromToken(token);
       return (
               username.equals(email)
                       && !isTokenExpired(token));
   }

   private Date calculateExpirationDate(Date createdDate, Long expirationDate) {
       return new Date(createdDate.getTime() + expirationDate * 1000);
   }
}

