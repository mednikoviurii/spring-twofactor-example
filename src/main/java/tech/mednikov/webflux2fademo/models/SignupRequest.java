package tech.mednikov.webflux2fademo.models;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;

}
