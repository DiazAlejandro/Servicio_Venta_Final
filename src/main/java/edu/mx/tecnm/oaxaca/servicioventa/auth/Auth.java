/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.auth;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aleja
 */
@Service
public class Auth {
    
    private final RestTemplate restTemplate;
    private final String AUTH_ENDPOINT = "https://autenticacion-t.herokuapp.com/login/auth/token/";

    public Auth(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    public boolean verifyToken(String token){
        String url = AUTH_ENDPOINT + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, Object> map = new HashMap<>();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity <Object> response = this.restTemplate.postForEntity(url, entity, Object.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
