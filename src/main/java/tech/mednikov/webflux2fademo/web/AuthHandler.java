package tech.mednikov.webflux2fademo.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import tech.mednikov.webflux2fademo.models.LoginRequest;
import tech.mednikov.webflux2fademo.models.LoginResponse;
import tech.mednikov.webflux2fademo.models.SignupRequest;
import tech.mednikov.webflux2fademo.models.SignupResponse;
import tech.mednikov.webflux2fademo.services.AuthService;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private MediaType json = MediaType.APPLICATION_JSON;
    private final AuthService service;

    public Mono<ServerResponse> signup (ServerRequest request){
        Mono<SignupRequest> body = request.bodyToMono(SignupRequest.class);
        Mono<SignupResponse> result = body.flatMap(service::signup);
        return ServerResponse.ok().contentType(json).body(result, SignupResponse.class);
    }

    public Mono<ServerResponse> login (ServerRequest request){
        Mono<LoginRequest> body = request.bodyToMono(LoginRequest.class);
        Mono<LoginResponse> result = body.flatMap(service::login);
        return ServerResponse.ok().contentType(json).body(result, LoginResponse.class);
    }
}
