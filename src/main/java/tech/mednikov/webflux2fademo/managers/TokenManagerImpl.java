package tech.mednikov.webflux2fademo.managers;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("TokenManager")
public class TokenManagerImpl implements TokenManager{

    private RSAKey key;

    public TokenManagerImpl() throws Exception{
        key =  new RSAKeyGenerator(2048).keyID(UUID.randomUUID().toString()).generate();
    }

    @Override
    public String issueToken(String userId) {
        try {
            JWSSigner signer = new RSASSASigner(key);
            JWTClaimsSet cs = new JWTClaimsSet.Builder().subject(userId).build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyID()).build(), cs);
            signedJWT.sign(signer);
            String token = signedJWT.serialize();
            return token;

        } catch (Exception ex){

            return null;

        }
    }
}
