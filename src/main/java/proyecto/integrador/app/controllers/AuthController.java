package proyecto.integrador.app.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import proyecto.integrador.app.config.jwt.JwtServiceImpl;
import proyecto.integrador.app.dto.request.AuthRequestDTO;
import proyecto.integrador.app.dto.response.AuthResponseDTO;
import proyecto.integrador.app.dto.request.RegisterRequestDTO;
import proyecto.integrador.app.dto.response.UserResponseDTO;
import proyecto.integrador.app.entities.RefreshToken;
import proyecto.integrador.app.exceptions.BadUserCredentialsException;
import proyecto.integrador.app.exceptions.ExpiredRefreshTokenException;
import proyecto.integrador.app.exceptions.ObjectNotFoundException;
import proyecto.integrador.app.repository.UserRepository;
import proyecto.integrador.app.services.auth.AuthenticationService;
import proyecto.integrador.app.services.auth.RefreshTokenService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type"})
public class AuthController {

    private final AuthenticationService authenticationService;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    private final JwtServiceImpl jwtService;

    private final UserRepository userRepository;

    public AuthController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService,
                          AuthenticationManager authenticationManager, JwtServiceImpl jwtService,
                          UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public UserResponseDTO addNewUser(@RequestBody RegisterRequestDTO registerRequestDTO){
        return authenticationService.saveUser(registerRequestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO getToken(@RequestBody AuthRequestDTO authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByUsername(authRequest.getEmail());
            refreshTokenOptional.ifPresent(refreshTokenService::DeleteRefreshToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());
            return AuthResponseDTO
                    .builder()
                    .accessToken(authenticationService.generateToken(authRequest.getEmail()))
                    .refreshToken(refreshToken.getToken())
                    .role(userRepository.findRoleByName(authRequest.getEmail()))
                    .build();

        }catch (BadUserCredentialsException e){
            throw new BadUserCredentialsException(e.getMessage());
        } catch (Exception e){
            throw new BadUserCredentialsException("Usuario y/o contraseña incorrectas");
        }

    }

    @PostMapping("/refreshToken")
    public AuthResponseDTO refreshToken(@RequestBody AuthResponseDTO authResponseDTO){

        return refreshTokenService.findByToken(authResponseDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userCredential -> {
                    String accessToken = null;
                    try {
                        accessToken = jwtService.generateToken(userCredential.getUsername());
                    } catch (ObjectNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return AuthResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(authResponseDTO.getRefreshToken()).build();
                }).orElseThrow(() ->new ExpiredRefreshTokenException("El refresh token no se encuentra en la base de datos"));
    }

    @GetMapping("/validateToken/{token}")
    public Map<String, Object> validateToken(@PathVariable(name = "token") String token){
        return authenticationService.validateToken(token);
    }

}
