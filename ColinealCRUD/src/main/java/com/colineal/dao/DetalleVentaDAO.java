
package com.colineal.dao;

import com.colineal.entities.DetalleVenta;
import com.colineal.entities.Venta;
import com.colineal.entities.Producto;
import jakarta.persistence.*;
import java.util.List;

public class DetalleVentaDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ColinealPU");

    
public void crear(DetalleVenta detalle) {
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        Venta venta = em.find(Venta.class, detalle.getVenta().getId());
        Producto producto = em.find(Producto.class, detalle.getProducto().getId());

        if (venta == null || producto == null) {
            throw new IllegalArgumentException(
                    "La venta o el producto no existen.");
        }

        Integer cantidad = detalle.getCantidad();

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        if (producto.getCantidad() < cantidad) {
            throw new IllegalArgumentException(
                    "No hay suficientes unidades en inventario.");
        }

        producto.setCantidad(producto.getCantidad() - cantidad);

        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setPrecioUnitario(producto.getPrecio());

        em.persist(detalle);
        em.getTransaction().commit();

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        throw new RuntimeException(
                "Error al registrar el detalle de venta.", e);

    } finally {
        em.close();
    }
}


    public List<DetalleVenta> obtenerTodos() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT d FROM DetalleVenta d ORDER BY d.id",
                    DetalleVenta.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
