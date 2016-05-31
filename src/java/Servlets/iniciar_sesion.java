package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Utils.EstadoSesion;
import Utils.Crypto;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Entities.Usuarios;
import ControladoresDAO.UsuarioController;
/**
 *
 * @author 43943085
 */
@WebServlet(urlPatterns = {"/iniciar-sesion"})
public class iniciar_sesion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession objSesion =request.getSession();
        String nickname = request.getParameter("txtNick");
        String password = request.getParameter("txtPwd");
        UsuarioController MT= new UsuarioController();
        try{
            Usuarios tec=MT.CheckLogIn(nickname, password);
            loginCorrecto(tec, objSesion, request, response);
        }catch(Error|Exception e){
            loginIncorrecto(objSesion, request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

        /**
     * Establece que el login fue incorrecto y despacha al servlet Login
     * @param objSesion
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void loginIncorrecto(HttpSession objSesion, 
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Establece que el login fue correcto y despacha a la página que redirige al home
     * @param usr
     * @param objSesion
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void loginCorrecto(Usuarios usr, HttpSession objSesion, 
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // setea el usuario logueado
        request.getSession().setAttribute("usuario_logueado", usr.getNickname());
        objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_CORRECTO);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
