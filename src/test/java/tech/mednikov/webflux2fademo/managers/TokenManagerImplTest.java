package tech.mednikov.webflux2fademo.managers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TokenManagerImplTest {

    private static TokenManagerImpl manager;

    @BeforeAll
    static void setup() throws Exception{
        manager = new TokenManagerImpl();
    }

    @Test
    void issueTokenTest(){
        final String userId = "userId";
        String token = manager.issueToken(userId);
        Assertions.assertThat(token).isNotNull();
    }
}
