/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Entities.Ombues;
import Entities.PuntoOmbu;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author Galvadion
 */
public class PuntoOmbuDAO extends AbstractDAO{
    
    public PuntoOmbuDAO(){
        super();
    }
    /**
     * Ingresa un usuario nuevo a la base de datos
     * @param usuario Usuario a ingresar a la base de datos
     */
    public void save(PuntoOmbu usuario) throws DataAccessLayerException {
        super.save(usuario);
    }
    
    public void saveWithFather(Ombues ombu, PuntoOmbu punto){
         try {
            startOperation();
            session.persist(ombu);
            session.flush();
             System.out.println(ombu);
            punto.setId(ombu.getId());
            punto.setOmbues(ombu);
            System.out.println(punto);
            session.persist(punto);
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
    }

    /**
     * Actualiza un usuario de la base
     * @param usuario Usuario a actualizar a la base con los datos ya modificados
     */
    public void update(PuntoOmbu usuario) throws DataAccessLayerException {
        super.update(usuario);
    }

    /**
     * Borra completamente al usuario 
     * @param usuario Usuario a eliminar de la base
     */
    public void delete(PuntoOmbu usuario) throws DataAccessLayerException {
        super.delete(usuario);
    }

    /**
     * Funcion que devuelve el usuario que coincida el ID pasado
     * @param id Long correspondinete al ID del usuario
     * @return Usuario si encuentra alguno
     */
    public PuntoOmbu find(Long id) throws DataAccessLayerException {
        return (PuntoOmbu) super.find(PuntoOmbu.class, id);
    }

    /**
     * Lista todos los usuarios de la base de datos
     * @return
     */
    public List findAll() throws DataAccessLayerException {
        return super.findAll(PuntoOmbu.class);
    }
}