/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.controller;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import edu.mx.tecnm.oaxaca.servicioventa.utils.CustomResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> registrarVenta
        (@RequestHeader(value = "Authorization", required = false) String token, 
                @RequestBody VentaModel venta) {
            ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();
        
        if(token == null){
            customResponse.setHttpCode(HttpStatus.UNAUTHORIZED);
            customResponse.setCode(401);
            customResponse.setMensaje("Favor enviar JWT en Headers como Authorization");
            responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
        }
        
        
        boolean flag = true;
        LinkedList atributes = new LinkedList();
        atributes.add("Campos que hacen falta:");
        if (venta.getCostoTotal() == 0.0d) {
            atributes.add("El atributo COSTO TOTAL no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        if (venta.getCantidadPagada()== 0.0d) {
            atributes.add("El atributo CANTIDAD PAGADA no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        if (venta.getCambio() == 0.0d) {
            atributes.add("El atributo CAMBIO no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        if (venta.getObservaciones() == null) {
            atributes.add("El atributo OBSERVACIONES no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        if (venta.getFecha() == null) {
            atributes.add("El atributo FECHA no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        if (venta.getEstado() == null) {
            atributes.add("El atributo ESTADI no puede ir vacío");
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            flag = false;
        }
        
        if (flag == true) {
            int noFolio = getVentasLastIndex().getId();
            String folio = "VENTA-"+(noFolio+1);
            venta.setFolio(folio);
            
            ArrayList data = new ArrayList();
            data.add(folio);
            ventaService.registarVenta(venta);
            customResponse.setHttpCode(HttpStatus.CREATED);
            customResponse.setCode(201);
            customResponse.setMensaje("Success");
            data.add(noFolio);
            customResponse.setData(data);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
        } else {
            customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
            customResponse.setCode(422);
            customResponse.setMensaje(atributes);
        }
        
        
        return responseEntity;
    }

    @GetMapping("/venta")
    public CustomResponse getVentas() {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVentas().isEmpty()) {
            customResponse.setHttpCode(HttpStatus.NO_CONTENT);
            customResponse.setMensaje("Not found Ventas in this table");
        } else {
            customResponse.setData(ventaService.getVentas());
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Showing all records");
        }
        return customResponse;
    }

    @GetMapping("/venta/{idVenta}")
    public CustomResponse getVenta(@PathVariable int idVenta) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVenta(idVenta) == null) {
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Ventas with id = " + idVenta);
        } else {
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
        customResponse.setHttpCode(HttpStatus.NO_CONTENT);
        customResponse.setCode(204);
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
    public CustomResponse getVentaFolio(@PathVariable String folio) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVentaByFolio(folio) == null) {
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Ventas with folio = " + folio);
            customResponse.setData(ventaService.getVentaByFolio(folio));
        } else {
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Show all matches with folio = " + folio);
            customResponse.setData(ventaService.getVentaByFolio(folio));
        }
        return customResponse;
    }

    @DeleteMapping("/venta/folio/{folio}")
    public CustomResponse deleteVentaByFolio(@PathVariable String folio) {
        CustomResponse customResponse = new CustomResponse();
        VentaModel venta = ventaService.getVentaByFolio(folio);
        if (venta == null) {
            customResponse.setHttpCode(HttpStatus.NOT_ACCEPTABLE);
            customResponse.setMensaje("This acction can't execute, Not found Ventas with folio = " + folio);
        } else {
            ventaService.deleteVenta(venta.getId());
            customResponse.setHttpCode(HttpStatus.ACCEPTED);
            customResponse.setMensaje("Delete success");
        }

        return customResponse;
    }

    @PutMapping("/venta/folio/{folio}")
    public CustomResponse updateVentaByFolio(@RequestBody VentaModel venta, @PathVariable String folio) {
        CustomResponse customResponse = new CustomResponse();
        VentaModel venta_model = ventaService.getVentaByFolio(folio);
        if (venta_model == null) {
            customResponse.setHttpCode(HttpStatus.NOT_ACCEPTABLE);
            customResponse.setMensaje("This acction can't execute, Not found Ventas with folio = " + folio);
        } else {
            ventaService.updateVenta(venta, venta_model.getId());
            customResponse.setHttpCode(HttpStatus.ACCEPTED);
            customResponse.setMensaje("Update success");
        }
        return customResponse;
    }

    public VentaModel getVentasLastIndex() {
        if (ventaService.getVentas().isEmpty()) {

        } else {
            int last_inx = ventaService.getVentas().size() - 1;
            List<VentaModel> vta = ventaService.getVentas();
            return vta.get(last_inx);
        }
        return null;
    }
}
