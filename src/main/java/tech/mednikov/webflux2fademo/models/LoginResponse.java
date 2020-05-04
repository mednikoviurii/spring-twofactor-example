package tech.mednikov.webflux2fademo.models;

import lombok.Data;

@Data
public class LoginResponse {

    private boolean success;
    private String userId;
    private String token;

}
