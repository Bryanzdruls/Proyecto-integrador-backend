package proyecto.integrador.app.services.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proyecto.integrador.app.entities.RefreshToken;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.exceptions.ExpiredRefreshTokenException;
import proyecto.integrador.app.repository.RefreshTokenRepository;
import proyecto.integrador.app.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        // Primero buscar si ya existe un refresh token para este usuario
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByEmail(email);

        if (existingToken.isPresent()) {
            // Actualizar el token existente
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpire));
            return refreshTokenRepository.save(refreshToken);
        }

        // Si no existe, crearlo
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpire))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new ExpiredRefreshTokenException("El token de actualización " + token.getToken() +  " esta vencido. Inicia sesión de nuevo");
        }
        return token;
    }



    public Optional<RefreshToken> findByUsername(String email){
        return refreshTokenRepository.findByEmail(email);
    }

    public void DeleteRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }
}
