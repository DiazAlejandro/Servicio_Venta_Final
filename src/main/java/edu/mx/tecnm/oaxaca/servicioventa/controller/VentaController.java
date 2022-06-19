/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.controller;

import edu.mx.tecnm.oaxaca.servicioventa.auth.Auth;
import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import edu.mx.tecnm.oaxaca.servicioventa.utils.CustomResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
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

    @Autowired
    private Auth auth;

    @PostMapping("/venta")
    public ResponseEntity<Object> registrarVenta(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody VentaModel venta) {

        ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();
        boolean flag = true;
        LinkedList atributes = new LinkedList();

        try {
            if (authorization == null) {
                customResponse.setHttpCode(HttpStatus.UNAUTHORIZED);
                customResponse.setCode(401);
                customResponse.setMensaje("Please, send a JWT Headers like Authorization");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
            }
            if (!auth.verifyToken(authorization)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse("JWT invalid or expired", 401));
            }
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new CustomResponse("Process invalid", 204));
            }
            if (venta.getCostoTotal() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("La cantidad a pagar tiene que ser mayor al costo total", 204));
            }

            if (getVentasLastIndex() == null) {
                String folio = "VENTA-1";
                ArrayList data = new ArrayList();

                venta.setFolio(folio);
                data.add(folio);
                ventaService.registarVenta(venta);
                customResponse.setHttpCode(HttpStatus.CREATED);
                customResponse.setCode(201);
                customResponse.setMensaje("Success");
                data.add(0);
                customResponse.setData(data);
                responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(customResponse);
            } else {
                int noFolio = getVentasLastIndex().getId();
                String folio = "VENTA-" + (noFolio + 1);
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
            }

        } catch (Exception e) {
            customResponse.setMensaje(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(customResponse);
        }

        return responseEntity;
    }

    @GetMapping("/venta")
    public ResponseEntity<Object> getVentas(@RequestHeader(value = "Authorization", required = false) String authorization) {
        ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();
        try {
            if (authorization == null) {
                customResponse.setHttpCode(HttpStatus.UNAUTHORIZED);
                customResponse.setCode(401);
                customResponse.setMensaje("Please, send a JWT Headers like Authorization");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
            }
            if (ventaService.getVentas().isEmpty()) {
                customResponse.setHttpCode(HttpStatus.NO_CONTENT);
                customResponse.setMensaje("Not found Ventas in this table");
                customResponse.setCode(204);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customResponse);
            } else {
                customResponse.setData(ventaService.getVentas());
                customResponse.setHttpCode(HttpStatus.OK);
                customResponse.setMensaje("Showing all records");
                customResponse.setCode(200);
                return ResponseEntity.status(HttpStatus.OK).body(customResponse);
            }
        } catch (Exception e) {
            customResponse.setMensaje(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(customResponse);
        }
    }

    @GetMapping("/venta/{idVenta}")
    public CustomResponse getVenta(@PathVariable int idVenta) {
        CustomResponse customResponse = new CustomResponse();
        if (ventaService.getVenta(idVenta) == null) {
            customResponse.setHttpCode(HttpStatus.NO_CONTENT);
            customResponse.setCode(204);
            customResponse.setMensaje("Not found Ventas with id = " + idVenta);
        } else {
            customResponse.setData(ventaService.getVenta(idVenta));
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setCode(200);
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
            customResponse.setHttpCode(HttpStatus.NO_CONTENT);
            customResponse.setCode(204);
            customResponse.setMensaje("Not found Ventas with folio = " + folio);
            customResponse.setData(ventaService.getVentaByFolio(folio));
        } else {
            customResponse.setHttpCode(HttpStatus.OK);
            customResponse.setCode(200);
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
