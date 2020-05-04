package tech.mednikov.webflux2fademo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import tech.mednikov.webflux2fademo.models.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByEmail (String email);

}
