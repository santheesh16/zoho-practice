package com.zoho.booktickets.service;

import java.security.Key;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.zoho.booktickets.constant.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtService {
    // Sample method to construct a JWT

    public long getMili() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 8);
        return calendar.getTimeInMillis();
    }

    private static String SECRET_KEY = Constant.TOKEN_SECRET_KEY;

    // Sample method to construct a JWT
    public static String createJWT(String id, String issuer, String subject) {
        long ttlMillis = new JwtService().getMili();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        Date exp = new Date(ttlMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public Claims decodeJWT(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public long expiryMin(LocalDate dateBefore) {
        long noOfDaysBetween = 0L;
        try {
            noOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(), dateBefore);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfDaysBetween;
    }

    public String parseJWT(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
        return claims.getIssuer();
    }

    public static void main(String[] args) {
        String token = JwtService.createJWT("1", "Santheesh", "Reset-Password");
        System.out.println(token);
    }
}