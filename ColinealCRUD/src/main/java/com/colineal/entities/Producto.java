package com.colineal.entities;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;



/**
 *
 * @author Mishell
 */
@Entity
@Table(name = "tb_producto")
@Getter
@Setter
@NoArgsConstructor
public class Producto {
   
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)    
@Column(name ="pr_id")
private Integer id;

@Column(name = "nombre", nullable = false)
private String nombre;

@Column(name = "descripcion")
private String descripcion;

@ManyToOne
@JoinColumn(name = "cat_id")
private Categoria categoria;

@Column(name = "pr_cantidad")
private Integer cantidad;

@Column(name = "pr_precio")
private Double precio;     

@Override
public String toString() {
    return nombre;
}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

   public Categoria getCategoria(){
       return categoria;
   }
   public void setCategoria(Categoria categoria){
       this.categoria = categoria;
       
   }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}



