/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.repository;

import edu.mx.tecnm.oaxaca.servicioventa.model.VentaModel;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author aleja
 */
@Repository
public interface VentaRepository extends JpaRepository<VentaModel, Integer>{
    public VentaModel findById(int idVenta);
    
    public VentaModel findByFolio(String folio);

}