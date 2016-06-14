/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.CategoriaJpaController;
import DAO.CategoriaReferenciasJpaController;
import DAO.ComentarioJpaController;
import DAO.ImagenesJpaController;
import DAO.OmbuesJpaController;
import DAO.ReferenciaOmbuJpaController;
import DAO.exceptions.PreexistingEntityException;
import Entities.Categoria;
import Entities.CategoriaReferencias;
import Entities.Comentario;
import Entities.Imagenes;
import Entities.Ombues;
import Entities.ReferenciaOmbu;
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
    CategoriaReferenciasJpaController crJPA=new CategoriaReferenciasJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    ComentarioJpaController coJPA=new ComentarioJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    ReferenciaOmbuJpaController roJPA=new ReferenciaOmbuJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    ImagenesJpaController imgJPA = new ImagenesJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    
    public int crearPuntoOmbu(Ombues ombu){
        return oJPA.saveAndGetId(ombu).getId();
    }
    
    public List<Categoria> categoriasList(){
        return cJPA.findCategoriaEntities();
    }
    
    public List<CategoriaReferencias> categoriaRefList(){
        return crJPA.findCategoriaReferenciasEntities();
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
    
    public void crearReferenciaOmbu(ReferenciaOmbu ref) throws PreexistingEntityException, Exception{
        roJPA.create(ref);
    }

    public CategoriaReferencias getCategoriaRefxId(Integer categoriaRefId) {
        return crJPA.findCategoriaReferencias(categoriaRefId);
    }
    
    public void OmbuImagenAgregar(int idOmbu, String nombreImagen){
        Ombues ombu = getOmbuxId(idOmbu);
        Imagenes img = new Imagenes();
        img.setNombre(nombreImagen);
        img.setOmbu(ombu);
        imgJPA.create(img);
    }
}
