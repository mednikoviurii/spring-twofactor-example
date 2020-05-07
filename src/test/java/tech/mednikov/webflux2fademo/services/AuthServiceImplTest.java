package tech.mednikov.webflux2fademo.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tech.mednikov.webflux2fademo.managers.TokenManager;
import tech.mednikov.webflux2fademo.managers.TotpManager;
import tech.mednikov.webflux2fademo.models.LoginRequest;
import tech.mednikov.webflux2fademo.models.SignupRequest;
import tech.mednikov.webflux2fademo.models.User;
import tech.mednikov.webflux2fademo.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private TokenManager tokenManager;
    @Mock private TotpManager totpManager;
    @Mock private UserRepository repository;

    @InjectMocks private AuthServiceImpl service;

    @Test
    void loginSuccessTest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("john.doe@mail.com");
        request.setPassword("secret");
        request.setCode("123456");

        final String salt = BCrypt.gensalt();
        final String hash = BCrypt.hashpw("secret", salt);

        User user = new User();
        user.setEmail("john.doe@mail.com");
        user.setHash(hash);
        user.setSalt(salt);
        user.setSecretKey("secretkey");
        user.setUserId("userId");

        Mono<User> source = Mono.just(user);
        Mockito.when(repository.findByEmail("john.doe@mail.com")).thenReturn(source);
        Mockito.when(totpManager.validateCode("123456", "secretkey")).thenReturn(true);
        Mockito.when(tokenManager.issueToken("userId")).thenReturn("token");

        StepVerifier.create(service.login(request))
                .assertNext(result -> Assertions.assertThat(result)
                        .hasFieldOrPropertyWithValue("success", true)
                        .hasFieldOrPropertyWithValue("token", "token")
                        .hasFieldOrPropertyWithValue("userId", "userId"))
                .verifyComplete();
    }

    @Test
    void loginDeniedTest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("john.doe@mail.com");
        request.setPassword("secret");
        request.setCode("123456");

        Mockito.when(repository.findByEmail("john.doe@mail.com")).thenReturn(Mono.empty());

        StepVerifier.create(service.login(request))
                .assertNext(result -> Assertions.assertThat(result.isSuccess()).isFalse())
                .verifyComplete();
    }

    @Test
    void signupSuccessTest(){
        SignupRequest request = new SignupRequest();
        request.setEmail("john.doe@mail.com");
        request.setPassword("secret");

        User user = new User();
        user.setEmail("john.doe@mail.com");
        user.setSecretKey("secretkey");
        user.setUserId("userId");

        Mockito.when(repository.findByEmail("john.doe@mail.com")).thenReturn(Mono.empty());
        Mockito.when(totpManager.generateSecret()).thenReturn("secretkey");
        Mockito.when(tokenManager.issueToken(Mockito.anyString())).thenReturn("token");
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(service.signup(request))
                .assertNext(result -> Assertions.assertThat(result)
                        .hasFieldOrPropertyWithValue("userId", "userId")
                        .hasFieldOrPropertyWithValue("success", true)
                        .hasFieldOrPropertyWithValue("token", "token")
                        .hasFieldOrPropertyWithValue("secretKey", "secretkey"))
                .verifyComplete();
    }

    @Test
    void signupDeniedTest(){
        SignupRequest request = new SignupRequest();
        request.setEmail("john.doe@mail.com");
        request.setPassword("secret");

        User user = new User();
        user.setEmail("john.doe@mail.com");
        user.setSecretKey("secretkey");
        user.setUserId("userId");

        Mockito.when(repository.findByEmail("john.doe@mail.com")).thenReturn(Mono.just(user));

        StepVerifier.create(service.signup(request))
                .assertNext(result -> Assertions.assertThat(result.isSuccess()).isFalse())
                .verifyComplete();
    }
}
