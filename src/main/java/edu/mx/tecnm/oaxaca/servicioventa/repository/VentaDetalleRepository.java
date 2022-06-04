/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.repository;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaDetalleModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author aleja
 */
public interface VentaDetalleRepository extends JpaRepository<VentaDetalleModel, Integer>{
    public VentaDetalleModel findById(int idDetalleVenta);   
    List<VentaDetalleModel> findByVentaId(int idVenta);
}