package tech.mednikov.webflux2fademo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {

//    private boolean success;
    private String userId;
    private String token;
    private String secretKey;

}
