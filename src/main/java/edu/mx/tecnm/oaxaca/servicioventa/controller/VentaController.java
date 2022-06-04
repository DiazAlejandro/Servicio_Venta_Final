/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.controller;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import edu.mx.tecnm.oaxaca.servicioventa.utils.CustomResponse;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author aleja
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @PostMapping("/venta")
    public CustomResponse registrarVenta(@RequestBody VentaModel venta) {
        CustomResponse customResponse = new CustomResponse();
        ventaService.registarVenta(venta);
        customResponse.setHttpCode(HttpStatus.CREATED);
        return customResponse;
    }
    
    @GetMapping("/venta")
    public CustomResponse getVentas() {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(ventaService.getVentas());
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }
    
    @GetMapping("/venta/{idVenta}")
    public CustomResponse getVenta(@PathVariable int idVenta) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(ventaService.getVenta(idVenta));
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }

    @PutMapping("/venta/{idVenta}")
    public CustomResponse updateVenta(@RequestBody VentaModel venta, @PathVariable Integer idVenta) {
        CustomResponse customResponse = new CustomResponse();
        ventaService.updateVenta(venta, idVenta);
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }

    @DeleteMapping("/venta/{idVenta}")
    public CustomResponse deleteCuenta(@PathVariable Integer idVenta) {
        CustomResponse customResponse = new CustomResponse();
        ventaService.deleteVenta(idVenta);
        customResponse.setHttpCode(HttpStatus.NO_CONTENT);
        return customResponse;
    }
}
