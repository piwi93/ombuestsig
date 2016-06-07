/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.CategoriaJpaController;
import DAO.ComentarioJpaController;
import DAO.OmbuesJpaController;
import Entities.Categoria;
import Entities.Comentario;
import Entities.Ombues;
import Entities.Usuarios;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author Galvadion
 */
public class PuntoOmbuController {
    OmbuesJpaController oJPA=new OmbuesJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    CategoriaJpaController cJPA=new CategoriaJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    ComentarioJpaController coJPA=new ComentarioJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    
    public int crearPuntoOmbu(Ombues ombu){
        return oJPA.saveAndGetId(ombu).getId();
    }
    
    public List<Categoria> categoriasList(){
        return cJPA.findCategoriaEntities();
    }
    
    public Categoria getCategoriaxId(Integer id){
        return cJPA.findCategoria(id);
    }
    
    public Ombues getOmbuxId(Integer id){
        return oJPA.findOmbues(id);
    }

    public void crearComentario(Ombues ombu, Usuarios user, String comentario) throws Exception {
        Comentario coment=new Comentario();
        coment.setComentario(comentario);
        Date hoy=new Date();
        coment.setFecha(hoy);
        coment.setIdOmbu(ombu);
        coment.setIdUser(user);
        coJPA.create(coment);
        ombu.getComentarioList().add(coment);
        oJPA.edit(ombu);
    }
}
