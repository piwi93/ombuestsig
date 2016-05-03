/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Entities.Usuarios;
import java.util.List;

/**
 *
 * @author Galvadion
 */
public class UsuariosDAO extends AbstractDAO{
    
    public UsuariosDAO(){
        super();
    }
    /**
     * Ingresa un usuario nuevo a la base de datos
     * @param usuario Usuario a ingresar a la base de datos
     */
    public void save(Usuarios usuario) throws DataAccessLayerException {
        super.save(usuario);
    }

    /**
     * Actualiza un usuario de la base
     * @param usuario Usuario a actualizar a la base con los datos ya modificados
     */
    public void update(Usuarios usuario) throws DataAccessLayerException {
        super.update(usuario);
    }

    /**
     * Borra completamente al usuario 
     * @param usuario Usuario a eliminar de la base
     */
    public void delete(Usuarios usuario) throws DataAccessLayerException {
        super.delete(usuario);
    }

    /**
     * Funcion que devuelve el usuario que coincida el ID pasado
     * @param id Long correspondinete al ID del usuario
     * @return Usuario si encuentra alguno
     */
    public Usuarios find(Long id) throws DataAccessLayerException {
        return (Usuarios) super.find(Usuarios.class, id);
    }

    /**
     * Lista todos los usuarios de la base de datos
     * @return
     */
    public List findAll() throws DataAccessLayerException {
        return super.findAll(Usuarios.class);
    }
}
