/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import Entities.CategoriaReferencias;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Galvadion
 */
public class CategoriaReferenciasJpaController implements Serializable {

    public CategoriaReferenciasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoriaReferencias categoriaReferencias) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(categoriaReferencias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoriaReferencias categoriaReferencias) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            categoriaReferencias = em.merge(categoriaReferencias);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoriaReferencias.getId();
                if (findCategoriaReferencias(id) == null) {
                    throw new NonexistentEntityException("The categoriaReferencias with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriaReferencias categoriaReferencias;
            try {
                categoriaReferencias = em.getReference(CategoriaReferencias.class, id);
                categoriaReferencias.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoriaReferencias with id " + id + " no longer exists.", enfe);
            }
            em.remove(categoriaReferencias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoriaReferencias> findCategoriaReferenciasEntities() {
        return findCategoriaReferenciasEntities(true, -1, -1);
    }

    public List<CategoriaReferencias> findCategoriaReferenciasEntities(int maxResults, int firstResult) {
        return findCategoriaReferenciasEntities(false, maxResults, firstResult);
    }

    private List<CategoriaReferencias> findCategoriaReferenciasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoriaReferencias.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CategoriaReferencias findCategoriaReferencias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriaReferencias.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaReferenciasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoriaReferencias> rt = cq.from(CategoriaReferencias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
