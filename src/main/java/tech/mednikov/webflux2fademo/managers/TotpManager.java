package tech.mednikov.webflux2fademo.managers;

public interface TotpManager {

    String generateSecret ();

    boolean validateCode (String code, String secret);

}
