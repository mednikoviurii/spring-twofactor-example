package tech.mednikov.webflux2fademo.services;

import reactor.core.publisher.Mono;
import tech.mednikov.webflux2fademo.models.LoginRequest;
import tech.mednikov.webflux2fademo.models.LoginResponse;
import tech.mednikov.webflux2fademo.models.SignupRequest;
import tech.mednikov.webflux2fademo.models.SignupResponse;

public interface AuthService {

    Mono<SignupResponse> signup (SignupRequest request);

    Mono<LoginResponse> login (LoginRequest request);

    Mono<String> parseToken (String token);

}
