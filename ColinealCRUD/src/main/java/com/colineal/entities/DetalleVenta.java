
package com.colineal.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "det_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ven_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "pr_id", nullable = false)
    private Producto producto;

    @Column(name = "det_cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "det_precio_unitario", nullable = false)
    private Double precioUnitario;

    public DetalleVenta() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecioUnitario() {
    return precioUnitario;
    }
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public Double calcularSubtotal() {
    return precioUnitario * cantidad;
}
}
