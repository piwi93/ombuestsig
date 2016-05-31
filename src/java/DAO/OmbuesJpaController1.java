/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Categoria;
import Entities.Imagenes;
import Entities.Ombues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * 
 * @author Nicolás Aquino <nicoaquin@hotmail.com>
 */
public class OmbuesJpaController1 implements Serializable {

    public OmbuesJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ombues ombues) {
        if (ombues.getImagenesCollection() == null) {
            ombues.setImagenesCollection(new ArrayList<Imagenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = ombues.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getId());
                ombues.setIdCategoria(idCategoria);
            }
            Collection<Imagenes> attachedImagenesCollection = new ArrayList<Imagenes>();
            for (Imagenes imagenesCollectionImagenesToAttach : ombues.getImagenesCollection()) {
                imagenesCollectionImagenesToAttach = em.getReference(imagenesCollectionImagenesToAttach.getClass(), imagenesCollectionImagenesToAttach.getId());
                attachedImagenesCollection.add(imagenesCollectionImagenesToAttach);
            }
            ombues.setImagenesCollection(attachedImagenesCollection);
            em.persist(ombues);
            if (idCategoria != null) {
                idCategoria.getOmbuesList().add(ombues);
                idCategoria = em.merge(idCategoria);
            }
            for (Imagenes imagenesCollectionImagenes : ombues.getImagenesCollection()) {
                Ombues oldIdOmbuOfImagenesCollectionImagenes = imagenesCollectionImagenes.getIdOmbu();
                imagenesCollectionImagenes.setIdOmbu(ombues);
                imagenesCollectionImagenes = em.merge(imagenesCollectionImagenes);
                if (oldIdOmbuOfImagenesCollectionImagenes != null) {
                    oldIdOmbuOfImagenesCollectionImagenes.getImagenesCollection().remove(imagenesCollectionImagenes);
                    oldIdOmbuOfImagenesCollectionImagenes = em.merge(oldIdOmbuOfImagenesCollectionImagenes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ombues ombues) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ombues persistentOmbues = em.find(Ombues.class, ombues.getId());
            Categoria idCategoriaOld = persistentOmbues.getIdCategoria();
            Categoria idCategoriaNew = ombues.getIdCategoria();
            Collection<Imagenes> imagenesCollectionOld = persistentOmbues.getImagenesCollection();
            Collection<Imagenes> imagenesCollectionNew = ombues.getImagenesCollection();
            List<String> illegalOrphanMessages = null;
            for (Imagenes imagenesCollectionOldImagenes : imagenesCollectionOld) {
                if (!imagenesCollectionNew.contains(imagenesCollectionOldImagenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Imagenes " + imagenesCollectionOldImagenes + " since its idOmbu field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getId());
                ombues.setIdCategoria(idCategoriaNew);
            }
            Collection<Imagenes> attachedImagenesCollectionNew = new ArrayList<Imagenes>();
            for (Imagenes imagenesCollectionNewImagenesToAttach : imagenesCollectionNew) {
                imagenesCollectionNewImagenesToAttach = em.getReference(imagenesCollectionNewImagenesToAttach.getClass(), imagenesCollectionNewImagenesToAttach.getId());
                attachedImagenesCollectionNew.add(imagenesCollectionNewImagenesToAttach);
            }
            imagenesCollectionNew = attachedImagenesCollectionNew;
            ombues.setImagenesCollection(imagenesCollectionNew);
            ombues = em.merge(ombues);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getOmbuesList().remove(ombues);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getOmbuesList().add(ombues);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Imagenes imagenesCollectionNewImagenes : imagenesCollectionNew) {
                if (!imagenesCollectionOld.contains(imagenesCollectionNewImagenes)) {
                    Ombues oldIdOmbuOfImagenesCollectionNewImagenes = imagenesCollectionNewImagenes.getIdOmbu();
                    imagenesCollectionNewImagenes.setIdOmbu(ombues);
                    imagenesCollectionNewImagenes = em.merge(imagenesCollectionNewImagenes);
                    if (oldIdOmbuOfImagenesCollectionNewImagenes != null && !oldIdOmbuOfImagenesCollectionNewImagenes.equals(ombues)) {
                        oldIdOmbuOfImagenesCollectionNewImagenes.getImagenesCollection().remove(imagenesCollectionNewImagenes);
                        oldIdOmbuOfImagenesCollectionNewImagenes = em.merge(oldIdOmbuOfImagenesCollectionNewImagenes);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<Imagenes> imagenesCollectionOrphanCheck = ombues.getImagenesCollection();
            for (Imagenes imagenesCollectionOrphanCheckImagenes : imagenesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ombues (" + ombues + ") cannot be destroyed since the Imagenes " + imagenesCollectionOrphanCheckImagenes + " in its imagenesCollection field has a non-nullable idOmbu field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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

}
