package tech.mednikov.webflux2fademo.models;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
    private String code;

}
