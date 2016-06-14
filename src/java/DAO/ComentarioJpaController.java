/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import Entities.Comentario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Ombues;
import Entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Galvadion
 */
public class ComentarioJpaController implements Serializable {

    public ComentarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comentario comentario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ombues idOmbu = comentario.getIdOmbu();
            if (idOmbu != null) {
                idOmbu = em.getReference(idOmbu.getClass(), idOmbu.getId());
                comentario.setIdOmbu(idOmbu);
            }
            Usuarios idUser = comentario.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                comentario.setIdUser(idUser);
            }
            em.persist(comentario);
            if (idOmbu != null) {
                idOmbu.getComentarioList().add(comentario);
                idOmbu = em.merge(idOmbu);
            }
            if (idUser != null) {
                idUser.getComentarioList().add(comentario);
                idUser = em.merge(idUser);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comentario comentario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario persistentComentario = em.find(Comentario.class, comentario.getId());
            Ombues idOmbuOld = persistentComentario.getIdOmbu();
            Ombues idOmbuNew = comentario.getIdOmbu();
            Usuarios idUserOld = persistentComentario.getIdUser();
            Usuarios idUserNew = comentario.getIdUser();
            if (idOmbuNew != null) {
                idOmbuNew = em.getReference(idOmbuNew.getClass(), idOmbuNew.getId());
                comentario.setIdOmbu(idOmbuNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                comentario.setIdUser(idUserNew);
            }
            comentario = em.merge(comentario);
            if (idOmbuOld != null && !idOmbuOld.equals(idOmbuNew)) {
                idOmbuOld.getComentarioList().remove(comentario);
                idOmbuOld = em.merge(idOmbuOld);
            }
            if (idOmbuNew != null && !idOmbuNew.equals(idOmbuOld)) {
                idOmbuNew.getComentarioList().add(comentario);
                idOmbuNew = em.merge(idOmbuNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getComentarioList().remove(comentario);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getComentarioList().add(comentario);
                idUserNew = em.merge(idUserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comentario.getId();
                if (findComentario(id) == null) {
                    throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.");
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
            Comentario comentario;
            try {
                comentario = em.getReference(Comentario.class, id);
                comentario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.", enfe);
            }
            Ombues idOmbu = comentario.getIdOmbu();
            if (idOmbu != null) {
                idOmbu.getComentarioList().remove(comentario);
                idOmbu = em.merge(idOmbu);
            }
            Usuarios idUser = comentario.getIdUser();
            if (idUser != null) {
                idUser.getComentarioList().remove(comentario);
                idUser = em.merge(idUser);
            }
            em.remove(comentario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comentario> findComentarioEntities() {
        return findComentarioEntities(true, -1, -1);
    }

    public List<Comentario> findComentarioEntities(int maxResults, int firstResult) {
        return findComentarioEntities(false, maxResults, firstResult);
    }

    private List<Comentario> findComentarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comentario.class));
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

    public Comentario findComentario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comentario.class, id);
        } finally {
            em.close();
        }
    }

    public int getComentarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comentario> rt = cq.from(Comentario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
