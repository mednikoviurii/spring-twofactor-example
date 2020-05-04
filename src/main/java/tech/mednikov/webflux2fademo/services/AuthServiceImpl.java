package tech.mednikov.webflux2fademo.services;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tech.mednikov.webflux2fademo.managers.TokenManager;
import tech.mednikov.webflux2fademo.managers.TotpManager;
import tech.mednikov.webflux2fademo.models.*;
import tech.mednikov.webflux2fademo.repositories.UserRepository;

@Component("AuthService")
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private TokenManager tokenManager;
    private TotpManager totpManager;
    private UserRepository repository;


    @Override
    public Mono<SignupResponse> signup(SignupRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword();
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(password, salt);
        String secret = totpManager.generateSecret();

        User user = new User(null, email, hash, salt, secret);

        Mono<SignupResponse> response = repository.findByEmail(email)
                .defaultIfEmpty(user)
                .flatMap(result -> {
                    if (result.getUserId() == null) {
                        return repository.save(result).flatMap(result2 -> {
                            String userId = result2.getUserId();
                            String token = tokenManager.issueToken(userId);
                            SignupResponse signupResponse = new SignupResponse();
                            signupResponse.setUserId(userId);
                            signupResponse.setSecretKey(secret);
                            signupResponse.setToken(token);
                            signupResponse.setSuccess(true);
                            return Mono.just(signupResponse);
                        });
                    } else {
                        SignupResponse signupResponse = new SignupResponse();
                        signupResponse.setSuccess(false);
                        return Mono.just(signupResponse);
                    }
                });
        return response;
    }

    @Override
    public Mono<LoginResponse> login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword();
        String code = request.getCode();
        Mono<LoginResponse> response = repository.findByEmail(email)
                .defaultIfEmpty(new User())
                .flatMap(user -> {
                    if (user.getUserId() == null) {
                        // no user
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.setSuccess(false);
                        return Mono.just(loginResponse);
                    } else {
                        // user exists
                        String salt = user.getSalt();
                        String secret = user.getSecretKey();
                        boolean passwordMatch = BCrypt.hashpw(password, salt).equalsIgnoreCase(user.getHash());
                        if (passwordMatch) {
                            // password matched
                            boolean codeMatched = totpManager.validateCode(code, secret);
                            if (codeMatched) {
                                String token = tokenManager.issueToken(user.getUserId());
                                LoginResponse loginResponse = new LoginResponse();
                                loginResponse.setSuccess(true);
                                loginResponse.setToken(token);
                                loginResponse.setUserId(user.getUserId());
                                return Mono.just(loginResponse);
                            } else {
                                LoginResponse loginResponse = new LoginResponse();
                                loginResponse.setSuccess(false);
                                return Mono.just(loginResponse);
                            }
                        } else {
                            LoginResponse loginResponse = new LoginResponse();
                            loginResponse.setSuccess(false);
                            return Mono.just(loginResponse);
                        }
                    }
                });
        return response;
    }
}
