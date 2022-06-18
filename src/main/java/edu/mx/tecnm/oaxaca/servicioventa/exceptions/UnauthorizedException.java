package edu.mx.tecnm.oaxaca.servicioventa.exceptions;

import edu.mx.tecnm.oaxaca.servicioventa.constants.AuthenticationConstans;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aleja
 */
public class UnauthorizedException extends CustomException{

    public UnauthorizedException() {
        super(AuthenticationConstans.INVALID_TOKEN_MENSAJE_EXCEPTION);
    }
    
}
