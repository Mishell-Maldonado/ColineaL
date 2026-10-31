package com.colineal.dao;

import com.colineal.entities.Categoria;
import jakarta.persistence.*;
import java.util.List;

public class CategoriaDao {

    public List<Categoria> obtenerTodas() {
        EntityManager em = Persistence
                .createEntityManagerFactory("ColinealPU")
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Categoria c ORDER BY c.nombre",
                    Categoria.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
    

public void crearIniciales() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ColinealPU");
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        String[] nombres = {
            "Salas",
            "Comedores",
            "Dormitorios",
            "Outdoors",
            "Colchones",
            "Decor Hogar"
        };

        for (String nombre : nombres) {
            Long existe = em.createQuery(
                    "SELECT COUNT(c) FROM Categoria c WHERE c.nombre = :nombre",
                    Long.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            if (existe == 0) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre);
                em.persist(categoria);
            }
        }

        em.getTransaction().commit();

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al crear las categorías.", e);

    } finally {
        em.close();
        emf.close();
    }
}
}



