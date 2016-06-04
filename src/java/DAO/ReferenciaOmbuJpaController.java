/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Ombues;
import Entities.ReferenciaOmbu;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Galvadion
 */
public class ReferenciaOmbuJpaController implements Serializable {

    public ReferenciaOmbuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReferenciaOmbu referenciaOmbu) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Ombues ombuesOrphanCheck = referenciaOmbu.getOmbues();
        if (ombuesOrphanCheck != null) {
            ReferenciaOmbu oldReferenciaOmbuOfOmbues = ombuesOrphanCheck.getReferenciaOmbu();
            if (oldReferenciaOmbuOfOmbues != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Ombues " + ombuesOrphanCheck + " already has an item of type ReferenciaOmbu whose ombues column cannot be null. Please make another selection for the ombues field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ombues ombues = referenciaOmbu.getOmbues();
            if (ombues != null) {
                ombues = em.getReference(ombues.getClass(), ombues.getId());
                referenciaOmbu.setOmbues(ombues);
            }
            em.persist(referenciaOmbu);
            if (ombues != null) {
                ombues.setReferenciaOmbu(referenciaOmbu);
                ombues = em.merge(ombues);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReferenciaOmbu(referenciaOmbu.getId()) != null) {
                throw new PreexistingEntityException("ReferenciaOmbu " + referenciaOmbu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReferenciaOmbu referenciaOmbu) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReferenciaOmbu persistentReferenciaOmbu = em.find(ReferenciaOmbu.class, referenciaOmbu.getId());
            Ombues ombuesOld = persistentReferenciaOmbu.getOmbues();
            Ombues ombuesNew = referenciaOmbu.getOmbues();
            List<String> illegalOrphanMessages = null;
            if (ombuesNew != null && !ombuesNew.equals(ombuesOld)) {
                ReferenciaOmbu oldReferenciaOmbuOfOmbues = ombuesNew.getReferenciaOmbu();
                if (oldReferenciaOmbuOfOmbues != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Ombues " + ombuesNew + " already has an item of type ReferenciaOmbu whose ombues column cannot be null. Please make another selection for the ombues field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ombuesNew != null) {
                ombuesNew = em.getReference(ombuesNew.getClass(), ombuesNew.getId());
                referenciaOmbu.setOmbues(ombuesNew);
            }
            referenciaOmbu = em.merge(referenciaOmbu);
            if (ombuesOld != null && !ombuesOld.equals(ombuesNew)) {
                ombuesOld.setReferenciaOmbu(null);
                ombuesOld = em.merge(ombuesOld);
            }
            if (ombuesNew != null && !ombuesNew.equals(ombuesOld)) {
                ombuesNew.setReferenciaOmbu(referenciaOmbu);
                ombuesNew = em.merge(ombuesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = referenciaOmbu.getId();
                if (findReferenciaOmbu(id) == null) {
                    throw new NonexistentEntityException("The referenciaOmbu with id " + id + " no longer exists.");
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
            ReferenciaOmbu referenciaOmbu;
            try {
                referenciaOmbu = em.getReference(ReferenciaOmbu.class, id);
                referenciaOmbu.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referenciaOmbu with id " + id + " no longer exists.", enfe);
            }
            Ombues ombues = referenciaOmbu.getOmbues();
            if (ombues != null) {
                ombues.setReferenciaOmbu(null);
                ombues = em.merge(ombues);
            }
            em.remove(referenciaOmbu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReferenciaOmbu> findReferenciaOmbuEntities() {
        return findReferenciaOmbuEntities(true, -1, -1);
    }

    public List<ReferenciaOmbu> findReferenciaOmbuEntities(int maxResults, int firstResult) {
        return findReferenciaOmbuEntities(false, maxResults, firstResult);
    }

    private List<ReferenciaOmbu> findReferenciaOmbuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReferenciaOmbu.class));
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

    public ReferenciaOmbu findReferenciaOmbu(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReferenciaOmbu.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferenciaOmbuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReferenciaOmbu> rt = cq.from(ReferenciaOmbu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
