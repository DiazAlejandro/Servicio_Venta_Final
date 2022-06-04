/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.implement.service;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import edu.mx.tecnm.oaxaca.servicioventa.repository.VentaRepository;
import edu.mx.tecnm.oaxaca.servicioventa.service.VentaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aleja
 */
@Service
public class VentaImplements implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Override
    public void registarVenta(VentaModel venta) {
        ventaRepository.save(venta);
    }

    @Override
    public List getVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public VentaModel getVenta(int idVenta) {
        return ventaRepository.findById(idVenta);
    }

    @Override
    public void updateVenta(VentaModel ventaModel, Integer idVenta) {
        ventaModel.setId(idVenta);
        ventaRepository.save(ventaModel);
    }

    @Override
    public void deleteVenta(Integer idVenta) {
        ventaRepository.deleteById(idVenta);
    }
    
}
