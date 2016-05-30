package Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author nico
 */
public enum EstadoSesion {
   NO_LOGIN,   //Nunca inicio sesiÃ³n
    LOGIN_CORRECTO, //Ha iniciado sesiÃ³n como tecnico
    LOGIN_INCORRECTO //Ha fallado al iniciar sesiÃ³n la menos 1 vez
}
