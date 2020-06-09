package tech.mednikov.webflux2fademo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {

    private final MediaType json = MediaType.APPLICATION_JSON;

    @Bean
    public RouterFunction<ServerResponse> authEndpoint (AuthHandler handler){
        return RouterFunctions
                .route(POST("/auth/signup").and(accept(json)), handler::signup)
                .andRoute(POST("/auth/login").and(accept(json)), handler::login);
    }

    @Bean
    public RouterFunction<ServerResponse> protectedEndpoint (ProtectedHandler handler,
                                                             AuthFunction authFunction){
        return RouterFunctions
                .route(GET("/secured/hello").and(accept(json)), handler::sayHello)
                .filter(authFunction::filter);
    }
}
