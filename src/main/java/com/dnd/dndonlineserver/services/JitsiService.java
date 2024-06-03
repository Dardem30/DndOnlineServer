package com.dnd.dndonlineserver.services;

import com.dnd.dndonlineserver.models.Room;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JitsiService {
    @Value("${jitsi.jwt.secret}")
    private String jitsiJwtSecret;
    @Value("${jitsi.jwt.lifespan}")
    private Integer jitsiJwtLifespan;
    @Value("${jitsi.jwt.app_id}")
    private String jitsiAppId;
    @Value("${jitsi.jwt.domain}")
    private String jitsiDomain;

    public String generateToken(final Room room) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("room", room.getId().toString());
        claims.put("aud", jitsiAppId);
        claims.put("iss", jitsiAppId);
        claims.put("sub", jitsiDomain);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * jitsiJwtLifespan))
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(jitsiJwtSecret.getBytes(StandardCharsets.UTF_8)))
//                .signWith(Keys.hmacShaKeyFor(jitsiJwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
//
//    public static void main(String[] args) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("sub", "jitsi-rlukashenko-dnd.online");
//        claims.put("room", "1");
//        claims.put("aud", "dnd-jitsi");
//        claims.put("iss", "dnd-jitsi");
//        System.out.println(
//                Jwts.builder()
////                        .setHeaderParam("typ", "JWT")
//                        .setClaims(claims)
//                        .setIssuedAt(new Date(System.currentTimeMillis()))
//                        .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 600))
//                        .signWith(Keys.hmacShaKeyFor("4OcMj9fHGTthkO2O9uD4xDnnllXkCKTG331SWok6o87uBDDn5U3Ca7AHb9ASMRic".getBytes(StandardCharsets.UTF_8)))
//                        .compact()
//        );
//    }
}
