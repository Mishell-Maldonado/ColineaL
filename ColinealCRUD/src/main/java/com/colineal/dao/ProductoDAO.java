
package com.colineal.dao;

import com.colineal.entities.Categoria;
import com.colineal.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;


public class ProductoDAO {
    
    private EntityManagerFactory emf;
    public ProductoDAO(){
        emf = Persistence.createEntityManagerFactory("ColinealPU");
       
    }
    
public void crear(Producto producto) {
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        if (producto.getCategoria() != null) {
            Categoria categoria = em.find(
                    Categoria.class,
                    producto.getCategoria().getId());

            producto.setCategoria(categoria);
        }

        em.persist(producto);
        em.getTransaction().commit();

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al crear el producto: "
                + e.getMessage(), e);
    } finally {
        em.close();
    }
}

   
    public void listar() {
        EntityManager em = emf.createEntityManager();

        try {
            var productos = em.createQuery(
                    "SELECT p FROM Producto p", Producto.class)
                    .getResultList();

            System.out.println("\n--- LISTA DE PRODUCTOS ---");

            for (Producto producto : productos) {
                System.out.println("ID: " + producto.getId());
                System.out.println("Nombre: " + producto.getNombre());
                System.out.println("Descripción: " + producto.getDescripcion());
                System.out.println("Categoría: " + producto.getCategoria());
                System.out.println("Cantidad: " + producto.getCantidad());
                System.out.println("Precio: $" + producto.getPrecio());
                System.out.println("--------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error al consultar los productos: "
                    + e.getMessage());

        } finally {
            em.close();
        }
    }

    public void actualizar(Producto producto) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Producto existente = em.find(Producto.class, producto.getId());

            if (existente != null) {
                existente.setNombre(producto.getNombre());
                existente.setDescripcion(producto.getDescripcion());
                existente.setCantidad(producto.getCantidad());
                existente.setPrecio(producto.getPrecio());
                Categoria categoria = em.find(
                    Categoria.class, producto.getCategoria().getId());
                existente.setCategoria(categoria);

                em.getTransaction().commit();
                System.out.println("Producto actualizado correctamente.");

            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el producto.");
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al actualizar: " + e.getMessage());

        } finally {
            em.close();
        }
    }

    public void eliminar(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Producto producto = em.find(Producto.class, id);

            if (producto != null) {
                em.remove(producto);
                em.getTransaction().commit();
                System.out.println("Producto eliminado correctamente.");

            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el producto con ID: " + id);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al eliminar: " + e.getMessage());

        } finally {
            em.close();
        }
    }
    
public void cerrar(){
    if (emf != null && emf.isOpen()){
        emf.close();
        
    }
}
 
public List<Producto> obtenerTodos() {
    EntityManager em = emf.createEntityManager();

    try {
        return em.createQuery(
            "SELECT p FROM Producto p ORDER BY p.id",
            Producto.class
        ).getResultList();
    } finally {
        em.close();
    }
}
 
public List<Producto> buscarPorCategoria(String nombreCategoria) {
    EntityManager em = emf.createEntityManager();

    try {
        return em.createQuery(
                "SELECT p FROM Producto p "
                + "WHERE p.categoria.nombre = :nombre",
                Producto.class)
                .setParameter("nombre", nombreCategoria)
                .getResultList();
    } finally {
        em.close();
    }
}  
}
