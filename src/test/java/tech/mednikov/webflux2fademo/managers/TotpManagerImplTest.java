package tech.mednikov.webflux2fademo.managers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TotpManagerImplTest {

    private static TotpManagerImpl manager;

    @BeforeAll
    static void setup() {
        manager = new TotpManagerImpl();
    }

    @Test
    void generateSecretTest(){
        String result = manager.generateSecret();
        Assertions.assertThat(result).isNotNull();
    }
}
