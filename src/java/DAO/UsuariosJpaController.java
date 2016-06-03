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
import Entities.Ombues;
import java.util.ArrayList;
import java.util.List;
import Entities.Comentario;
import Entities.Usuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Galvadion
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getOmbuesList() == null) {
            usuarios.setOmbuesList(new ArrayList<Ombues>());
        }
        if (usuarios.getComentarioList() == null) {
            usuarios.setComentarioList(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ombues> attachedOmbuesList = new ArrayList<Ombues>();
            for (Ombues ombuesListOmbuesToAttach : usuarios.getOmbuesList()) {
                ombuesListOmbuesToAttach = em.getReference(ombuesListOmbuesToAttach.getClass(), ombuesListOmbuesToAttach.getId());
                attachedOmbuesList.add(ombuesListOmbuesToAttach);
            }
            usuarios.setOmbuesList(attachedOmbuesList);
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : usuarios.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getId());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            usuarios.setComentarioList(attachedComentarioList);
            em.persist(usuarios);
            for (Ombues ombuesListOmbues : usuarios.getOmbuesList()) {
                Usuarios oldIdUsuarioOfOmbuesListOmbues = ombuesListOmbues.getIdUsuario();
                ombuesListOmbues.setIdUsuario(usuarios);
                ombuesListOmbues = em.merge(ombuesListOmbues);
                if (oldIdUsuarioOfOmbuesListOmbues != null) {
                    oldIdUsuarioOfOmbuesListOmbues.getOmbuesList().remove(ombuesListOmbues);
                    oldIdUsuarioOfOmbuesListOmbues = em.merge(oldIdUsuarioOfOmbuesListOmbues);
                }
            }
            for (Comentario comentarioListComentario : usuarios.getComentarioList()) {
                Usuarios oldIdUserOfComentarioListComentario = comentarioListComentario.getIdUser();
                comentarioListComentario.setIdUser(usuarios);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldIdUserOfComentarioListComentario != null) {
                    oldIdUserOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldIdUserOfComentarioListComentario = em.merge(oldIdUserOfComentarioListComentario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            List<Ombues> ombuesListOld = persistentUsuarios.getOmbuesList();
            List<Ombues> ombuesListNew = usuarios.getOmbuesList();
            List<Comentario> comentarioListOld = persistentUsuarios.getComentarioList();
            List<Comentario> comentarioListNew = usuarios.getComentarioList();
            List<Ombues> attachedOmbuesListNew = new ArrayList<Ombues>();
            for (Ombues ombuesListNewOmbuesToAttach : ombuesListNew) {
                ombuesListNewOmbuesToAttach = em.getReference(ombuesListNewOmbuesToAttach.getClass(), ombuesListNewOmbuesToAttach.getId());
                attachedOmbuesListNew.add(ombuesListNewOmbuesToAttach);
            }
            ombuesListNew = attachedOmbuesListNew;
            usuarios.setOmbuesList(ombuesListNew);
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getId());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            usuarios.setComentarioList(comentarioListNew);
            usuarios = em.merge(usuarios);
            for (Ombues ombuesListOldOmbues : ombuesListOld) {
                if (!ombuesListNew.contains(ombuesListOldOmbues)) {
                    ombuesListOldOmbues.setIdUsuario(null);
                    ombuesListOldOmbues = em.merge(ombuesListOldOmbues);
                }
            }
            for (Ombues ombuesListNewOmbues : ombuesListNew) {
                if (!ombuesListOld.contains(ombuesListNewOmbues)) {
                    Usuarios oldIdUsuarioOfOmbuesListNewOmbues = ombuesListNewOmbues.getIdUsuario();
                    ombuesListNewOmbues.setIdUsuario(usuarios);
                    ombuesListNewOmbues = em.merge(ombuesListNewOmbues);
                    if (oldIdUsuarioOfOmbuesListNewOmbues != null && !oldIdUsuarioOfOmbuesListNewOmbues.equals(usuarios)) {
                        oldIdUsuarioOfOmbuesListNewOmbues.getOmbuesList().remove(ombuesListNewOmbues);
                        oldIdUsuarioOfOmbuesListNewOmbues = em.merge(oldIdUsuarioOfOmbuesListNewOmbues);
                    }
                }
            }
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    comentarioListOldComentario.setIdUser(null);
                    comentarioListOldComentario = em.merge(comentarioListOldComentario);
                }
            }
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Usuarios oldIdUserOfComentarioListNewComentario = comentarioListNewComentario.getIdUser();
                    comentarioListNewComentario.setIdUser(usuarios);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldIdUserOfComentarioListNewComentario != null && !oldIdUserOfComentarioListNewComentario.equals(usuarios)) {
                        oldIdUserOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldIdUserOfComentarioListNewComentario = em.merge(oldIdUserOfComentarioListNewComentario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<Ombues> ombuesList = usuarios.getOmbuesList();
            for (Ombues ombuesListOmbues : ombuesList) {
                ombuesListOmbues.setIdUsuario(null);
                ombuesListOmbues = em.merge(ombuesListOmbues);
            }
            List<Comentario> comentarioList = usuarios.getComentarioList();
            for (Comentario comentarioListComentario : comentarioList) {
                comentarioListComentario.setIdUser(null);
                comentarioListComentario = em.merge(comentarioListComentario);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuariosxNick(String nick) {
        EntityManager em = getEntityManager();
        try {
            Query qry = em.createQuery("SELECT a FROM Usuarios a WHERE a.nickname = :nick ");
            qry.setParameter("nick", nick);
            return (Usuarios) qry.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
    }
}
