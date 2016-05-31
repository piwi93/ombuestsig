/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import DAO.exceptions.NonexistentEntityException;
import Entities.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Ombues;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * 
 * @author Nicolás Aquino <nicoaquin@hotmail.com>
 */
public class CategoriaJpaController1 implements Serializable {

    public CategoriaJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) {
        if (categoria.getOmbuesList() == null) {
            categoria.setOmbuesList(new ArrayList<Ombues>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ombues> attachedOmbuesList = new ArrayList<Ombues>();
            for (Ombues ombuesListOmbuesToAttach : categoria.getOmbuesList()) {
                ombuesListOmbuesToAttach = em.getReference(ombuesListOmbuesToAttach.getClass(), ombuesListOmbuesToAttach.getId());
                attachedOmbuesList.add(ombuesListOmbuesToAttach);
            }
            categoria.setOmbuesList(attachedOmbuesList);
            em.persist(categoria);
            for (Ombues ombuesListOmbues : categoria.getOmbuesList()) {
                Categoria oldIdCategoriaOfOmbuesListOmbues = ombuesListOmbues.getIdCategoria();
                ombuesListOmbues.setIdCategoria(categoria);
                ombuesListOmbues = em.merge(ombuesListOmbues);
                if (oldIdCategoriaOfOmbuesListOmbues != null) {
                    oldIdCategoriaOfOmbuesListOmbues.getOmbuesList().remove(ombuesListOmbues);
                    oldIdCategoriaOfOmbuesListOmbues = em.merge(oldIdCategoriaOfOmbuesListOmbues);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getId());
            List<Ombues> ombuesListOld = persistentCategoria.getOmbuesList();
            List<Ombues> ombuesListNew = categoria.getOmbuesList();
            List<Ombues> attachedOmbuesListNew = new ArrayList<Ombues>();
            for (Ombues ombuesListNewOmbuesToAttach : ombuesListNew) {
                ombuesListNewOmbuesToAttach = em.getReference(ombuesListNewOmbuesToAttach.getClass(), ombuesListNewOmbuesToAttach.getId());
                attachedOmbuesListNew.add(ombuesListNewOmbuesToAttach);
            }
            ombuesListNew = attachedOmbuesListNew;
            categoria.setOmbuesList(ombuesListNew);
            categoria = em.merge(categoria);
            for (Ombues ombuesListOldOmbues : ombuesListOld) {
                if (!ombuesListNew.contains(ombuesListOldOmbues)) {
                    ombuesListOldOmbues.setIdCategoria(null);
                    ombuesListOldOmbues = em.merge(ombuesListOldOmbues);
                }
            }
            for (Ombues ombuesListNewOmbues : ombuesListNew) {
                if (!ombuesListOld.contains(ombuesListNewOmbues)) {
                    Categoria oldIdCategoriaOfOmbuesListNewOmbues = ombuesListNewOmbues.getIdCategoria();
                    ombuesListNewOmbues.setIdCategoria(categoria);
                    ombuesListNewOmbues = em.merge(ombuesListNewOmbues);
                    if (oldIdCategoriaOfOmbuesListNewOmbues != null && !oldIdCategoriaOfOmbuesListNewOmbues.equals(categoria)) {
                        oldIdCategoriaOfOmbuesListNewOmbues.getOmbuesList().remove(ombuesListNewOmbues);
                        oldIdCategoriaOfOmbuesListNewOmbues = em.merge(oldIdCategoriaOfOmbuesListNewOmbues);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getId();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<Ombues> ombuesList = categoria.getOmbuesList();
            for (Ombues ombuesListOmbues : ombuesList) {
                ombuesListOmbues.setIdCategoria(null);
                ombuesListOmbues = em.merge(ombuesListOmbues);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
