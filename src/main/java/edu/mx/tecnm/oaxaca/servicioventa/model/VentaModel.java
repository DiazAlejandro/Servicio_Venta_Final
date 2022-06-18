/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.mx.tecnm.oaxaca.servicioventa.model;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
/**
 *
 * @author aleja
 */
@Entity
@Table(name = "ventas")
public class VentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "folio")
    @NotBlank(message = "{NotBlank.venta.folio}")
    private String folio;

    @Column(name = "costoTotal")
    private double costoTotal;

    @Column(name = "cantidadPagada")
    private double cantidadPagada;
    
    @Column(name = "cambio")
    private double cambio;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "fecha")
    private String fecha;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "estatusDelete")
    private boolean estatusDelete;
    
    @Column(name = "rfc")
    private String rfc;
    

    public VentaModel() {
    }

    public VentaModel(Integer id, String folio, double costoTotal, 
            double cantidadPagada, double cambio, String observaciones, 
            String fecha, String estado, boolean estatusDelete, 
            String rfc) {
        this.id = id;
        this.folio = folio;
        this.costoTotal = costoTotal;
        this.cantidadPagada = cantidadPagada;
        this.cambio = cambio;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.estado = estado;
        this.estatusDelete = estatusDelete;
        this.rfc = rfc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public double getCantidadPagada() {
        return cantidadPagada;
    }

    public void setCantidadPagada(double cantidadPagada) {
        this.cantidadPagada = cantidadPagada;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isEstatusDelete() {
        return estatusDelete;
    }

    public void setEstatusDelete(boolean estatusDelete) {
        this.estatusDelete = estatusDelete;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Override
    public String toString() {
        return "VentaModel{" + "id=" + id + ", folio=" + 
                folio + ", costoTotal=" + costoTotal + ", cantidadPagada=" + 
                cantidadPagada + ", cambio=" + cambio + ", observaciones=" + 
                observaciones + ", fecha=" + fecha + ", estado=" + 
                estado + ", estatusDelete=" + estatusDelete + ", rfc=" + 
                rfc +'}';
    }
    
    
}
