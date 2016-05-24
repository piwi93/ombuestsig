/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.OmbuesJpaController;
import Entities.Ombues;
import javax.persistence.Persistence;

/**
 *
 * @author Galvadion
 */
public class PuntoOmbuController {
    OmbuesJpaController oJPA=new OmbuesJpaController(Persistence.createEntityManagerFactory("TSIGPU"));
    
    
    public int crearPuntoOmbu(Ombues ombu){
        return oJPA.saveAndGetId(ombu).getId();
    }
    
    
    
}
