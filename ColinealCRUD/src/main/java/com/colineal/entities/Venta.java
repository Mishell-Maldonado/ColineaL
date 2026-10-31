
package com.colineal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "tb_venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ven_id")
    private Integer id;

    @Column(name = "ven_fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "cli_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles = new ArrayList<>();
    
    public Double calcularTotal() {
    double total = 0;

    for (DetalleVenta detalle : detalles) {
        total += detalle.calcularSubtotal();
    }

    return total;
    }
    
    public Venta() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public List<DetalleVenta> getDetalles() {
    return detalles;
    }
}
