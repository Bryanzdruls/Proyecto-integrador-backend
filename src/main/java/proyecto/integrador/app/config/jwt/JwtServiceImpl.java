package proyecto.integrador.app.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.exceptions.ObjectNotFoundException;
import proyecto.integrador.app.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET;

    private final UserRepository userRepository;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String,Object> validateToken(final String token){
        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            response.put("isValid", true);
            return response;
        } catch (JwtException e) {
            response.put("isValid", false);
            return response;
        }
    }

    public String generateToken(String username) throws ObjectNotFoundException {
        Map<String,Object> claims = new HashMap<>();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));
        claims.put("role", user.getRole());
        claims.put("userId",user.getIdUser());
        return createToke(claims,username);
    }


    private String createToke(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpire))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
