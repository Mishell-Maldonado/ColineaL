
package com.colineal.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.OneToMany;
@Entity
@Table(name = "tb_cliente")
public class Cliente {
    
    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Integer id;

    @Column(name = "cli_nombre", nullable = false)
    private String nombre;

    @Column(name = "cli_cedula", unique = true)
    private String cedula;

    @Column(name = "cli_telefono")
    private String telefono;

    @Column(name = "cli_correo")
    private String correo;
    
    @Override
    public String toString() {
        return nombre;
    }

    public Cliente() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public List<Venta> getVentas() {
    return ventas;
    }
}
