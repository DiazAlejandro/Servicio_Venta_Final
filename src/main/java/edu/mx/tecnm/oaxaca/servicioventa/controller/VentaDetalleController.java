/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.controller;

import edu.mx.tecnm.oaxaca.servicioventa.auth.Auth;
import edu.mx.tecnm.oaxaca.servicioventa.model.VentaDetalleModel;
import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaDetalleRepository;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaRepository;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaDetalleService;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import edu.mx.tecnm.oaxaca.servicioventa.utils.CustomResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author aleja
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VentaDetalleController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaDetalleService ventaDetalleService;

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Autowired
    private Auth auth;

    @PostMapping("/venta/{idVenta}/ventadetalle")
    public ResponseEntity<Object> registrarVentaDetalle(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idVenta, @RequestBody VentaDetalleModel ventaDetalle) {
        CustomResponse customResponse = new CustomResponse();
        ResponseEntity<Object> responseEntity = null;
        try {
            if (authorization == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse(HttpStatus.UNAUTHORIZED,
                                "Please, send a JWT Headers like Authorization",
                                401));
            }
            if (!auth.verifyToken(authorization)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse("JWT invalid or expired", 401));
            }
            VentaModel ventaModel = ventaService.getVenta(idVenta);
            if (ventaModel != null) {
                ventaDetalle.setVenta(ventaModel);
                ventaDetalleService.registarVentaDetalle(ventaDetalle);
                return ResponseEntity.status(HttpStatus.CREATED).body(new CustomResponse(HttpStatus.CREATED, "Success", 201));
            } else {
                customResponse.setMensaje("There isn't a sale with the id " + idVenta);
                customResponse.setHttpCode(HttpStatus.UNPROCESSABLE_ENTITY);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new CustomResponse(HttpStatus.UNPROCESSABLE_ENTITY, 
                                "There isn't a sale with the id " + idVenta,422));
            }

        } catch (DataIntegrityViolationException e) {
            customResponse.setMensaje("Error with ID");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(customResponse);
        } catch (HttpClientErrorException e) {
            customResponse.setMensaje("JWT invalid or expired");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(customResponse);
        } catch (Exception e) {
            customResponse.setMensaje(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(customResponse);
        }
    }

    @GetMapping("/venta/{idVenta}/ventadetalle")
    public ResponseEntity<Object> getVentaDetalle(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable int idVenta) {
        ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();
        try {
            if (authorization == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse(HttpStatus.UNAUTHORIZED,
                                "Please, send a JWT Headers like Authorization", 401));
            }
            if (!auth.verifyToken(authorization)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse("JWT invalid or expired", 401));
            }
            if (!(idVenta + "").matches("-?\\d+")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("Param can not be a strrng", 400));
            }

            List<VentaDetalleModel> detalles = ventaDetalleRepository.findByVentaId(idVenta);
            if (detalles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.NO_CONTENT, detalles,
                                "\"Not found Detalles in this table with idVenta" + idVenta, 204));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK, detalles, "Showing all matches", 200));
            }

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new CustomResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                            "JWT invalid or expired", 422)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new CustomResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                            e.getMessage().toString(), 422));
        }

    }

    @GetMapping("/ventadetalle")
    public CustomResponse getDetalle() {
        CustomResponse customResponse = new CustomResponse();
        if (ventaDetalleService.getVentasDetalle().isEmpty()) {
            customResponse.setData(ventaDetalleService.getVentasDetalle());
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Detalles in this table");
        } else {
            customResponse.setData(ventaDetalleService.getVentasDetalle());
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Showing all records");
        }

        return customResponse;
    }

    @GetMapping("/ventadetalle/{idDetalle}")
    public CustomResponse getDetalle(@PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaDetalleService.getVentaDetalle(idDetalle) == null) {
            customResponse.setData(ventaDetalleService.getVentaDetalle(idDetalle));
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("No matches");
        } else {
            customResponse.setData(ventaDetalleService.getVentaDetalle(idDetalle));
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setMensaje("Showing data");
        }
        return customResponse;
    }

    @DeleteMapping("/ventadetalle/{idDetalle}")
    public CustomResponse deleteDetalle(@PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaDetalleService.getVentaDetalle(idDetalle) == null) {
            customResponse.setHttpCode(HttpStatus.NOT_FOUND);
            customResponse.setMensaje("Not found Detalles in this table with idDetalle " + idDetalle);
        } else {
            ventaDetalleService.deleteVentaDetalle(idDetalle);
            customResponse.setHttpCode(HttpStatus.ACCEPTED);
            customResponse.setMensaje("Delete uccess");

        }
        return customResponse;
    }

    @PutMapping("/ventadetalle/{idDetalle}")
    public CustomResponse updateDetalle(@RequestBody VentaDetalleModel ventaDetalle, @PathVariable int idDetalle) {
        CustomResponse customResponse = new CustomResponse();
        ventaDetalleService.updateVentaDetalle(ventaDetalle, idDetalle);
        customResponse.setHttpCode(HttpStatus.OK);
        customResponse.setMensaje("Update Success");
        return customResponse;
    }
}
