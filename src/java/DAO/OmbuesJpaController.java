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
import Entities.Usuarios;
import Entities.Comentario;
import java.util.ArrayList;
import java.util.List;
import Entities.Imagenes;
import Entities.Ombues;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * 
 * @author Nicolás Aquino <nicoaquin@hotmail.com>
 */
public class OmbuesJpaController implements Serializable {

    public OmbuesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ombues ombues) {
        if (ombues.getComentarioList() == null) {
            ombues.setComentarioList(new ArrayList<Comentario>());
        }
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
            Usuarios idUsuario = ombues.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                ombues.setIdUsuario(idUsuario);
            }
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : ombues.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getId());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            ombues.setComentarioList(attachedComentarioList);
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
            if (idUsuario != null) {
                idUsuario.getOmbuesList().add(ombues);
                idUsuario = em.merge(idUsuario);
            }
            for (Comentario comentarioListComentario : ombues.getComentarioList()) {
                Ombues oldIdOmbuOfComentarioListComentario = comentarioListComentario.getIdOmbu();
                comentarioListComentario.setIdOmbu(ombues);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldIdOmbuOfComentarioListComentario != null) {
                    oldIdOmbuOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldIdOmbuOfComentarioListComentario = em.merge(oldIdOmbuOfComentarioListComentario);
                }
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
            Usuarios idUsuarioOld = persistentOmbues.getIdUsuario();
            Usuarios idUsuarioNew = ombues.getIdUsuario();
            List<Comentario> comentarioListOld = persistentOmbues.getComentarioList();
            List<Comentario> comentarioListNew = ombues.getComentarioList();
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
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                ombues.setIdUsuario(idUsuarioNew);
            }
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getId());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            ombues.setComentarioList(comentarioListNew);
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
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getOmbuesList().remove(ombues);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getOmbuesList().add(ombues);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    comentarioListOldComentario.setIdOmbu(null);
                    comentarioListOldComentario = em.merge(comentarioListOldComentario);
                }
            }
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Ombues oldIdOmbuOfComentarioListNewComentario = comentarioListNewComentario.getIdOmbu();
                    comentarioListNewComentario.setIdOmbu(ombues);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldIdOmbuOfComentarioListNewComentario != null && !oldIdOmbuOfComentarioListNewComentario.equals(ombues)) {
                        oldIdOmbuOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldIdOmbuOfComentarioListNewComentario = em.merge(oldIdOmbuOfComentarioListNewComentario);
                    }
                }
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
            Usuarios idUsuario = ombues.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getOmbuesList().remove(ombues);
                idUsuario = em.merge(idUsuario);
            }
            List<Comentario> comentarioList = ombues.getComentarioList();
            for (Comentario comentarioListComentario : comentarioList) {
                comentarioListComentario.setIdOmbu(null);
                comentarioListComentario = em.merge(comentarioListComentario);
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
