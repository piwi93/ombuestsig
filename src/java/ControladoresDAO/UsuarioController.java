/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import DAO.UsuariosJpaController;
import Entities.Usuarios;
import Utils.Crypto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;

/**
 *
 * @author Galvadion
 */
public class UsuarioController {

    private final Crypto encript = new Crypto();
    private UsuariosJpaController uJPA = new UsuariosJpaController(Persistence.createEntityManagerFactory("TSIGPU"));

    public Usuarios CheckLogIn(String nick, String password) {
        try {
            Usuarios tec = uJPA.findUsuariosxNick(nick);
            if (encript.generatePwd(password).equals(tec.getPassword())) {
                return tec;
            } else {
                throw new Error("La contraseña no coincide");
            }
        } catch (NullPointerException e) {
            throw new Error("El usuario no existe");
        }

    }

    public void CambiarPassword(Usuarios tec, String password) {
        tec.setPassword(encript.generatePwd(password));
        try {
            uJPA.edit(tec);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
