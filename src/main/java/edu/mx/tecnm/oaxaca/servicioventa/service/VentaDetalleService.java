/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.service;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaDetalleModel;
import java.util.List;

/**
 *
 * @author aleja
 */
public interface VentaDetalleService {
    public void registarVentaDetalle(VentaDetalleModel venta);
    public List getVentasDetalle();
    public VentaDetalleModel getVentaDetalle(int idVenta);
    public void updateVentaDetalle(VentaDetalleModel ventaDetalleModel, Integer idDetalleVenta);
    public void deleteVentaDetalle(Integer idDetalleVenta);
}