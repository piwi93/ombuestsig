/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.OmbuesDAO;
import DAO.PuntoOmbuDAO;
import Entities.Ombues;
import Entities.PuntoOmbu;

/**
 *
 * @author Galvadion
 */
public class PuntoOmbuController {
    private final PuntoOmbuDAO poDAO=new PuntoOmbuDAO();
    private final OmbuesDAO oDAO=new OmbuesDAO();
    
    
    public int crearPuntoOmbu(Ombues ombu){
        return oDAO.saveAndGetId(ombu).getId();
    }
    
    
    
}
