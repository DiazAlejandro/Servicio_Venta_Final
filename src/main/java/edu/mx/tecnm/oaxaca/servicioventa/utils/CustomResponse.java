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
    public Object mensaje;
    public int code;

    public CustomResponse(HttpStatus httpCode, int code) {
        this.httpCode = httpCode;
        this.code = code;
        data = new LinkedList();
        this.mensaje = new LinkedList();
    }

    public CustomResponse(Object mensaje, int code) {
        this.httpCode = httpCode;
        this.data = data;
        this.mensaje = mensaje;
        this.code = code;
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

    public Object getMensaje() {
        return mensaje;
    }

    public void setMensaje(Object mensaje) {
        this.mensaje = mensaje;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
}


