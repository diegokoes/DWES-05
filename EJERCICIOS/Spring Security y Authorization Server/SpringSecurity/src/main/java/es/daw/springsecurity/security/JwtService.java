package es.daw.springsecurity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/*
JwtService

Genera tokens JWT con una clave secreta.
Extrae información del token.
Valida si el token pertenece al usuario y si está expirado.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    // --------------------------
    //private final String SECRET_KEY = "tu_secreta_clave_super_segura"; // Usa una clave segura (idealmente en variables de entorno)


    private final String SECRET_KEY = generateSecureKey().toString();

    private SecretKey generateSecureKey() {
        return Jwts.SIG.HS256.key().build(); // 🔥 Genera una clave segura de 256 bits
    }

    // --------------------------

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {

        // 🔥 Forzar a JJWT a cargar su implementación
        ensureJjwtImplIsLoaded();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validez
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /** truqui del almendruqui **/
    private void ensureJjwtImplIsLoaded() {
        try {
            Class.forName("io.jsonwebtoken.impl.DefaultJwtBuilder");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JJWT implementation not found! Ensure jjwt-impl is in the classpath.", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
