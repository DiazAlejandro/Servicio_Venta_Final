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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse(HttpStatus.UNAUTHORIZED,
                                "Please, send a JWT Headers like Authorization",
                                401));
            }
            if (!auth.verifyToken(authorization)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new CustomResponse("JWT invalid or expired", 401));
            }
            if ((venta.getCambio() + "").isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new CustomResponse("Process invalid", 204));
            }
            if ((venta.getCantidadPagada() - venta.getCostoTotal()) < venta.getCambio()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "El cambio no puede ser mayor que la cantidad pagada menos el costo total", 204));
            }
            if (venta.getCostoTotal() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "La cantidad a pagar tiene que ser mayor al costo total", 400));
            }

            if (venta.getCambio() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("El cambio no puede ser mayor que la cantidad pagada", 400));
            }
            if (!(venta.getRfc().length() == 13)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("La longitud del RFC tiene que ser 13", 400));
            }

            String folio = "";
            ArrayList data = new ArrayList();
            int noFolio;

            if (getVentasLastIndex() == null) {
                noFolio = 0;
                folio = "VENTA-1";
            } else {
                noFolio = getVentasLastIndex().getId();
                folio = "VENTA-" + (noFolio + 1);
            }
            venta.setFolio(folio);
            data.add(folio);
            ventaService.registarVenta(venta);
            customResponse.setHttpCode(HttpStatus.CREATED);
            customResponse.setCode(201);
            customResponse.setMensaje("Success");
            data.add(noFolio);
            customResponse.setData(data);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(customResponse);

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

        return responseEntity;
    }

    @GetMapping("/venta")
    public ResponseEntity<Object> getVentas(@RequestHeader(value = "Authorization", required = false) String authorization) {
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
            if (ventaService.getVentas().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new CustomResponse(HttpStatus.NO_CONTENT,
                                "Not found Ventas in this table", 204));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK,
                                ventaService.getVentas(), "Showing all records", 200));
            }
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new CustomResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                            "JWT invalid or expired", 422));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new CustomResponse(HttpStatus.UNPROCESSABLE_ENTITY,
                            e.getMessage(), 422));
        }
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<Object> getVentas(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable int idVenta) {
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

            if (ventaService.getVenta(idVenta) == null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.NO_CONTENT,
                                "Not found Ventas with id = " + idVenta, 204));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK, ventaService.getVenta(idVenta), "Showing all matches", 200));
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

    @PutMapping("/venta/{idVenta}")
    public ResponseEntity<Object> updateVenta(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody VentaModel venta, @PathVariable Integer idVenta) {
        ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();

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
            if ((venta.getCambio() + "").isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new CustomResponse("Process invalid", 204));
            }
            if ((venta.getCantidadPagada() - venta.getCostoTotal()) < venta.getCambio()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "El cambio no puede ser mayor que la cantidad pagada menos el costo total", 204));
            }
            if (venta.getCostoTotal() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "La cantidad a pagar tiene que ser mayor al costo total", 400));
            }
            if (venta.getCambio() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("El cambio no puede ser mayor que la cantidad pagada", 400));
            }
            if (!(venta.getRfc().length() == 13)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("La longitud del RFC tiene que ser 13", 400));
            }
            
            if (ventaService.getVenta(idVenta) == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                        new CustomResponse(HttpStatus.NOT_ACCEPTABLE, 
                                "This acction can't execute, Not found Ventas with id = "+idVenta, 406 ));
            }
                ventaService.updateVenta(venta, idVenta);
                customResponse.setHttpCode(HttpStatus.OK);
                customResponse.setCode(200);
                customResponse.setMensaje("Update Success");
                return ResponseEntity.status(HttpStatus.OK).body(customResponse);
                        
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

    @DeleteMapping("/venta/{idVenta}")
    public ResponseEntity<Object> deleteCuenta(@RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer idVenta) {
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
            if (ventaService.getVenta(idVenta) == null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.NO_CONTENT,
                                "Not found Ventas with id = " + idVenta, 204));
            } else {
                ventaService.deleteVenta(idVenta);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK, "Delete success", 200));
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

    @GetMapping("/venta/folio/{folio}")
    public ResponseEntity<Object> getVentaFolio(@RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable String folio) {
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
            if (ventaService.getVentaByFolio(folio) == null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.NO_CONTENT,
                                "Not found Ventas with folio = " + folio, 204));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK, ventaService.getVentaByFolio(folio), "Showing all matches", 200));
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

    @DeleteMapping("/venta/folio/{folio}")
    public ResponseEntity<Object> deleteVentaByFolio(@RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable String folio) {
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
            VentaModel venta = ventaService.getVentaByFolio(folio);
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.NO_CONTENT,
                                "Not found Ventas with id = " + folio, 204));
            } else {
                ventaService.deleteVenta(venta.getId());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new CustomResponse(HttpStatus.OK, "Delete success", 200));
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

    @PutMapping("/venta/folio/{folio}")
    public ResponseEntity<Object> updateVentaByFolio(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody VentaModel venta, @PathVariable String folio) {
        ResponseEntity<Object> responseEntity = null;
        CustomResponse customResponse = new CustomResponse();

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
            if ((venta.getCambio() + "").isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new CustomResponse("Process invalid", 204));
            }
            if ((venta.getCantidadPagada() - venta.getCostoTotal()) < venta.getCambio()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "El cambio no puede ser mayor que la cantidad pagada menos el costo total", 204));
            }
            if (venta.getCostoTotal() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse(HttpStatus.BAD_REQUEST,
                                "La cantidad a pagar tiene que ser mayor al costo total", 400));
            }
            if (venta.getCambio() > venta.getCantidadPagada()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("El cambio no puede ser mayor que la cantidad pagada", 400));
            }
            if (!(venta.getRfc().length() == 13)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new CustomResponse("La longitud del RFC tiene que ser 13", 400));
            }
            
            if (ventaService.getVentaByFolio(folio) == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                        new CustomResponse(HttpStatus.NOT_ACCEPTABLE, 
                                "This acction can't execute, Not found Ventas with folio = "+folio, 406 ));
            }
                ventaService.updateVenta(venta, ventaService.getVentaByFolio(folio).getId());
                customResponse.setHttpCode(HttpStatus.OK);
                customResponse.setCode(204);
                customResponse.setMensaje("Update Success");
                return ResponseEntity.status(HttpStatus.OK).body(customResponse);
                        
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
