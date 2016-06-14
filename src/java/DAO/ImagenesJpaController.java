/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import DAO.exceptions.NonexistentEntityException;
import Entities.Imagenes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Ombues;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * 
 * @author Nicolás Aquino <nicoaquin@hotmail.com>
 */
public class ImagenesJpaController implements Serializable {

    public ImagenesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Imagenes imagenes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ombues idOmbu = imagenes.getOmbu();
            if (idOmbu != null) {
                idOmbu = em.getReference(idOmbu.getClass(), idOmbu.getId());
                imagenes.setOmbu(idOmbu);
            }
            em.persist(imagenes);
            if (idOmbu != null) {
                idOmbu.getImagenesCollection().add(imagenes);
                idOmbu = em.merge(idOmbu);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Imagenes imagenes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imagenes persistentImagenes = em.find(Imagenes.class, imagenes.getId());
            Ombues idOmbuOld = persistentImagenes.getOmbu();
            Ombues idOmbuNew = imagenes.getOmbu();
            if (idOmbuNew != null) {
                idOmbuNew = em.getReference(idOmbuNew.getClass(), idOmbuNew.getId());
                imagenes.setOmbu(idOmbuNew);
            }
            imagenes = em.merge(imagenes);
            if (idOmbuOld != null && !idOmbuOld.equals(idOmbuNew)) {
                idOmbuOld.getImagenesCollection().remove(imagenes);
                idOmbuOld = em.merge(idOmbuOld);
            }
            if (idOmbuNew != null && !idOmbuNew.equals(idOmbuOld)) {
                idOmbuNew.getImagenesCollection().add(imagenes);
                idOmbuNew = em.merge(idOmbuNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = imagenes.getId();
                if (findImagenes(id) == null) {
                    throw new NonexistentEntityException("The imagenes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imagenes imagenes;
            try {
                imagenes = em.getReference(Imagenes.class, id);
                imagenes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The imagenes with id " + id + " no longer exists.", enfe);
            }
            Ombues idOmbu = imagenes.getOmbu();
            if (idOmbu != null) {
                idOmbu.getImagenesCollection().remove(imagenes);
                idOmbu = em.merge(idOmbu);
            }
            em.remove(imagenes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Imagenes> findImagenesEntities() {
        return findImagenesEntities(true, -1, -1);
    }

    public List<Imagenes> findImagenesEntities(int maxResults, int firstResult) {
        return findImagenesEntities(false, maxResults, firstResult);
    }

    private List<Imagenes> findImagenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Imagenes.class));
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

    public Imagenes findImagenes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Imagenes.class, id);
        } finally {
            em.close();
        }
    }

    public int getImagenesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Imagenes> rt = cq.from(Imagenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
