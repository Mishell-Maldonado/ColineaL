
package com.colineal.dao;

import com.colineal.entities.Cliente;
import jakarta.persistence.*;
import java.util.List;

public class ClienteDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ColinealPU");

    public void crear(Cliente cliente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public List<Cliente> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c ORDER BY c.id",
                    Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }
    public void eliminar(Integer id) {
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        Cliente cliente = em.find(Cliente.class, id);

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no existe.");
        }

        em.remove(cliente);
        em.getTransaction().commit();

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException(
                "No se pudo eliminar el cliente.", e);
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
