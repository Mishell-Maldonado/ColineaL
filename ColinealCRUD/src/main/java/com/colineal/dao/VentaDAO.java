
package com.colineal.dao;

import com.colineal.entities.Venta;
import com.colineal.entities.Cliente;
import jakarta.persistence.*;
import java.util.List;

public class VentaDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ColinealPU");

    public void crear(Venta venta) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            if (venta.getFecha() == null) {
                    venta.setFecha(java.time.LocalDate.now());
                }
            Cliente cliente = em.find(
                    Cliente.class, venta.getCliente().getId());

            if (cliente == null) {
                throw new IllegalArgumentException(
                        "El cliente no existe.");
            }

            venta.setCliente(cliente);
            em.persist(venta);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(
                    "Error al guardar la venta.", e);
        } finally {
            em.close();
        }
    }

    public List<Venta> obtenerTodas() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT v FROM Venta v ORDER BY v.id",
                    Venta.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
    
    public List<Venta> buscarPorCliente(Integer idCliente) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT v FROM Venta v WHERE v.cliente.id = :id",
                    Venta.class)
                    .setParameter("id", idCliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
