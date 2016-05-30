/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.CategoriaJpaController;
import DAO.OmbuesJpaController;
import Entities.Categoria;
import Entities.Ombues;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author Galvadion
 */
public class PuntoOmbuController {
    OmbuesJpaController oJPA=new OmbuesJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    CategoriaJpaController cJPA=new CategoriaJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    
    public int crearPuntoOmbu(Ombues ombu){
        return oJPA.saveAndGetId(ombu).getId();
    }
    
    public List<Categoria> categoriasList(){
        return cJPA.findCategoriaEntities();
    }
    
    public Categoria getCategoriaxId(Integer id){
        return cJPA.findCategoria(id);
    }
}
