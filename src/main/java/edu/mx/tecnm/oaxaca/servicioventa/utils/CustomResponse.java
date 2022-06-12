/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.utils;

import java.util.LinkedList;
import org.springframework.http.HttpStatus;

/**
 *
 * @author aleja
 */
public class CustomResponse {

    private HttpStatus httpCode;
    private Object data;
    public String mensaje;
    public int code;

    public CustomResponse(HttpStatus httpCode, int code) {
        this.httpCode = httpCode;
        data = new LinkedList();
        this.mensaje = "OK";
    }

    public CustomResponse() {
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(HttpStatus httpCode) {
        this.httpCode = httpCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
}


