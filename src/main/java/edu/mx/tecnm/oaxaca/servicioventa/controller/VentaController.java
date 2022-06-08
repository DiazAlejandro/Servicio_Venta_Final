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
        customResponse.setMensaje("Success");
        return customResponse;
    }
    
    @GetMapping("/venta")
    public CustomResponse getVentas() {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVentas().isEmpty()){
            customResponse.setHttpCode(HttpStatus.NO_CONTENT);
            customResponse.setMensaje("Not found Ventas in this table");
        }else{
            customResponse.setData(ventaService.getVentas());
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Showing all records");
        }
        return customResponse;
    }
    
    @GetMapping("/venta/{idVenta}")
    public CustomResponse getVenta(@PathVariable int idVenta) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVenta(idVenta) == null){
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Ventas with id = "+idVenta);
        }else{
            customResponse.setData(ventaService.getVenta(idVenta));
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Showing all matches");
        }
        return customResponse;
    }

    @PutMapping("/venta/{idVenta}")
    public CustomResponse updateVenta(@RequestBody VentaModel venta, @PathVariable Integer idVenta) {
        CustomResponse customResponse = new CustomResponse();
        ventaService.updateVenta(venta, idVenta);
        customResponse.setHttpCode(HttpStatus.OK);
        customResponse.setMensaje("Update Success");
        return customResponse;
    }

    @DeleteMapping("/venta/{idVenta}")
    public CustomResponse deleteCuenta(@PathVariable Integer idVenta) {
        CustomResponse customResponse = new CustomResponse();
        ventaService.deleteVenta(idVenta);
        customResponse.setHttpCode(HttpStatus.NO_CONTENT);
        customResponse.setMensaje("Delete success");
        return customResponse;
    }
    
    @GetMapping("/venta/folio/{folio}")
    public CustomResponse getVentaFolio (@PathVariable String folio) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVentaByFolio(folio) == null){
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Ventas with folio = "+folio);
            customResponse.setData(ventaService.getVentaByFolio(folio));
        }else{
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Show all matches with folio = "+folio);
            customResponse.setData(ventaService.getVentaByFolio(folio));
        }
        return customResponse;
    }
    
    @DeleteMapping("/venta/folio/{folio}")
    public CustomResponse deleteVentaByFolio (@PathVariable String folio) {
        CustomResponse customResponse = new CustomResponse();
        VentaModel venta = ventaService.getVentaByFolio(folio);
        ventaService.deleteVenta(venta.getId());
        customResponse.setHttpCode(HttpStatus.NO_CONTENT);
        customResponse.setMensaje("Delete success");
        return customResponse;
    }
}