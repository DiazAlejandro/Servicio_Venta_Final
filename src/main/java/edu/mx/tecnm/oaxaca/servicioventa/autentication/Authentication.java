/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.autentication;

import edu.mx.tecnm.oaxaca.servicioventa.constants.AuthenticationConstans;
import edu.mx.tecnm.oaxaca.servicioventa.enums.TipoRespuestaParseEnum;
import edu.mx.tecnm.oaxaca.servicioventa.exceptions.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author aleja
 */
@Component
public class Authentication {
    @Autowired
    private HttpRequest http;
    
    public void auth(HttpServletRequest request) throws UnauthorizedException, ExternalMicroserviceException, IOException {
        Optional<String> tokenOptional = Optional.ofNullable(request.getHeader("Authorization"));
        if (!tokenOptional.isPresent())
            throw new UnauthorizedException();
        
        Map<String, Object> result = validateRequestTokenVerficacion(tokenOptional.get());
        
    }
    
    
    private Map<String, Object> validateRequestTokenVerficacion(String token) throws IOException {
        Map<String, Object> basicRequest = http.createBasicDataRequest(TipoRespuestaParseEnum.MAP);
        basicRequest.put("url", AuthenticationConstans.URL_AUTH + token);

        Map<String, String> body = new HashMap();
        basicRequest.put("body", body);

        Map<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        basicRequest.put("headers", headers);

        Map<String, Object> result = http.post(basicRequest);
        return result;
    }
}
