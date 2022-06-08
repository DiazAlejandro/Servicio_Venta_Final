/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.service;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author aleja
 */
@Service
public interface VentaService {
    public void registarVenta(VentaModel venta);
    public List getVentas();
    public VentaModel getVenta(int idVenta);
    public void updateVenta(VentaModel ventaModel, Integer idVenta);
    public void deleteVenta(Integer idVenta);
    public VentaModel getVentaByFolio(String folio);
    
    public void updateVentaByFolio(VentaModel ventaModel, String folio);
    public void deleteVentaByFolio(String folio);
}