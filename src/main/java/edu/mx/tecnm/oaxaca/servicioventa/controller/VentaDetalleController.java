/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.controller;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaDetalleModel;
import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaDetalleRepository;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaRepository;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaDetalleService;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import edu.mx.tecnm.oaxaca.servicioventa.utils.CustomResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author aleja
 */
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VentaDetalleController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaDetalleService ventaDetalleService;
    
    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @PostMapping("/venta/{idVenta}/ventadetalle")
    public CustomResponse registrarVentaDetalle(@PathVariable Integer idVenta,
            @RequestBody VentaDetalleModel ventaDetalle) {
        CustomResponse customResponse = new CustomResponse();
        VentaModel ventaModel = ventaService.getVenta(idVenta);
        if (ventaModel != null){
            ventaDetalle.setVenta(ventaModel);
            ventaDetalleService.registarVentaDetalle(ventaDetalle);
        }
        customResponse.setHttpCode(HttpStatus.CREATED);
        return customResponse;
    }
    
    @GetMapping("/venta/{idVenta}/ventadetalle")
    public CustomResponse getVentaDetalle(@PathVariable int idVenta) {
        CustomResponse customResponse = new CustomResponse();
        List<VentaDetalleModel> detalles = ventaDetalleRepository.findByVentaId(idVenta);
        customResponse.setData(detalles);
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }
    
    @GetMapping("/ventadetalle")
    public CustomResponse getDetalle() {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(ventaDetalleService.getVentasDetalle());
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }
    
    @GetMapping("/ventadetalle/{idDetalle}")
    public CustomResponse getDetalle(@PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(ventaDetalleService.getVentaDetalle(idDetalle));
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }
    
    @DeleteMapping("/ventadetalle/{idDetalle}")
    public CustomResponse deleteDetalle(@PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        ventaDetalleService.deleteVentaDetalle(idDetalle);
        customResponse.setHttpCode(HttpStatus.NO_CONTENT);
        return customResponse;
    }
    
    @PutMapping("/ventadetalle/{idDetalle}")
    public CustomResponse updateDetalle(@RequestBody VentaDetalleModel ventaDetalle, @PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        ventaDetalleService.updateVentaDetalle(ventaDetalle, idDetalle);
        customResponse.setHttpCode(HttpStatus.OK);
        return customResponse;
    }
}
