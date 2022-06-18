/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.autentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.mx.tecnm.oaxaca.servicioventa.enums.TipoRespuestaParseEnum;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Component;

/**
 *
 * @author aleja
 */
@Component
public class HttpRequest {

    public Map<String, Object> get(Map<String, Object> dataRequest) throws IOException {
        String url = dataRequest.get("url").toString();
        Map<String, String> headers = (Map<String, String>) dataRequest.get("headers");
        TipoRespuestaParseEnum type_response = (TipoRespuestaParseEnum) dataRequest.get("responseParseTO");
        TypeReference responseParseTO = getTypeResponse(type_response);

        Builder request = creteClientRequestHttp(url);
        addHeaders(request, headers);
        Response response = request.get();

        return parseResponse(response, responseParseTO);
    }
    
    Map<String, Object> createBasicDataRequest(TipoRespuestaParseEnum typeResponse) {
        Map<String, Object> basicDataRequest = new LinkedHashMap();
        Map<String, String> headers = new LinkedHashMap<>();
        basicDataRequest.put("url", "");
        basicDataRequest.put("responseParseTO", typeResponse);
        basicDataRequest.put("headers", headers);
        basicDataRequest.put("body", new Object());
        return basicDataRequest;
    }

    public Map<String, Object> post(Map<String, Object> dataRequest) throws IOException{
        String url = dataRequest.get("url").toString();
        Object body = dataRequest.get("body");
        Map<String, String> headers = (Map<String, String>) dataRequest.get("headers");
        TipoRespuestaParseEnum type_response = (TipoRespuestaParseEnum) dataRequest.get("responseParseTO");
        TypeReference responseParseTO = getTypeResponse(type_response);

        Builder request = creteClientRequestHttp(url);
        addHeaders(request, headers);
        Response response = request.post( Entity.entity(
                                body,
                                MediaType.APPLICATION_JSON_TYPE
                        ));
        
        Map<String,Object> responseMap = new HashMap();
        
        if(type_response == TipoRespuestaParseEnum.STRING){
            responseMap.put("body", response.readEntity(String.class));
        }else{
            responseMap =  parseResponse(response, responseParseTO);
        }

        return responseMap;
    }
    
    private TypeReference getTypeResponse(TipoRespuestaParseEnum typeResponse) {
        TypeReference responseParseTO = null;
        switch (typeResponse) {
            case LIST:
                responseParseTO = new TypeReference<List>() {
                };
                break;
            case MAP:
                responseParseTO = new TypeReference<Map>() {
                };
                break;
        }
        return responseParseTO;
    }
    
    private Map parseResponse(Response response, TypeReference responseParseTO) throws IOException {
        Map<String, Object> responseData = new LinkedHashMap();
        String result = response.readEntity(String.class);
        responseData.put("status", response.getStatus());

        if (response.getStatus() == 200 || response.getStatus() == 401) {
            ObjectMapper mapper = new ObjectMapper();
            Object responseBody = mapper.readValue(result, responseParseTO);
            responseData.put("body", responseBody);
        } else {
            responseData.put("body", result);
        }

        return responseData;
    }

    private Builder creteClientRequestHttp(String url) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Builder request = target.request(MediaType.APPLICATION_JSON);
        return request;
    }
    
    private void addHeaders(Builder request, Map<String, String> headers) {
        headers.entrySet().forEach(entry -> {
            String key = entry.getKey();
            String val = entry.getValue();
            request.header(key, val);
        });
    }

    public String createBasicAuthentication(String user, String password) {
        String token = user + ":" + password;
        try {
            return "BASIC " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
    }
}
