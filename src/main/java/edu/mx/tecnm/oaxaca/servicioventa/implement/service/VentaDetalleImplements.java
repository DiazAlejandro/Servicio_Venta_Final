/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.implement.service;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaDetalleModel;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaDetalleRepository;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaDetalleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aleja
 */
@Service
public class VentaDetalleImplements implements VentaDetalleService{

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;
            
    @Override
    public void registarVentaDetalle(VentaDetalleModel venta) {
        ventaDetalleRepository.save(venta);
    }

    @Override
    public List getVentasDetalle() {
        return ventaDetalleRepository.findAll();
    }

    @Override
    public VentaDetalleModel getVentaDetalle(int idVenta) {
        return ventaDetalleRepository.findById(idVenta);
    }

    @Override
    public void updateVentaDetalle(VentaDetalleModel ventaDetalleModel, Integer idDetalleVenta) {
        ventaDetalleModel.setId(idDetalleVenta);
        ventaDetalleRepository.save(ventaDetalleModel);
    }

    @Override
    public void deleteVentaDetalle(Integer idDetalleVenta) {
        ventaDetalleRepository.deleteById(idDetalleVenta);
    }
    
}
