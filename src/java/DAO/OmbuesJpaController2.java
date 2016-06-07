/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Categoria;
import Entities.Ombues;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Galvadion
 */
public class OmbuesJpaController2 implements Serializable {

    public OmbuesJpaController2(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ombues ombues) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = ombues.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getId());
                ombues.setIdCategoria(idCategoria);
            }
            em.persist(ombues);
            if (idCategoria != null) {
                idCategoria.getOmbuesList().add(ombues);
                idCategoria = em.merge(idCategoria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ombues ombues) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ombues persistentOmbues = em.find(Ombues.class, ombues.getId());
            Categoria idCategoriaOld = persistentOmbues.getIdCategoria();
            Categoria idCategoriaNew = ombues.getIdCategoria();
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getId());
                ombues.setIdCategoria(idCategoriaNew);
            }
            ombues = em.merge(ombues);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getOmbuesList().remove(ombues);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getOmbuesList().add(ombues);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ombues.getId();
                if (findOmbues(id) == null) {
                    throw new NonexistentEntityException("The ombues with id " + id + " no longer exists.");
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
            Ombues ombues;
            try {
                ombues = em.getReference(Ombues.class, id);
                ombues.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ombues with id " + id + " no longer exists.", enfe);
            }
            Categoria idCategoria = ombues.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getOmbuesList().remove(ombues);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(ombues);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ombues> findOmbuesEntities() {
        return findOmbuesEntities(true, -1, -1);
    }

    public List<Ombues> findOmbuesEntities(int maxResults, int firstResult) {
        return findOmbuesEntities(false, maxResults, firstResult);
    }

    private List<Ombues> findOmbuesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ombues.class));
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

    public Ombues findOmbues(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ombues.class, id);
        } finally {
            em.close();
        }
    }

    public int getOmbuesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ombues> rt = cq.from(Ombues.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Ombues saveAndGetId(Ombues ombu) {
        EntityManager em=getEntityManager();
         em.getTransaction().begin();
        em.persist(ombu);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return ombu;
    }
    
}
