/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ControladoresDAO.UsuarioController;
import DAO.exceptions.PreexistingEntityException;
import Entities.Usuarios;
import Utils.Crypto;
import Utils.EstadoSesion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author RedMasPc
 */
@WebServlet(urlPatterns = {"/userRegister"})
public class UserRegister extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserRegister</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserRegister at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        HttpSession session = request.getSession();
        try{
        if(session.getAttribute("estado_sesion").equals(EstadoSesion.LOGIN_CORRECTO)){
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }}catch(Exception e){
            
        }
        request.getRequestDispatcher("/WEB-INF/user_register.jsp").forward(request, response);

     

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
        try {
            //processRequest(request, response);
            String userName = request.getParameter("userName");
            String userPassword = request.getParameter("password");
            UsuarioController UC = new UsuarioController();
            Crypto encript = new Crypto();
            Usuarios user = new Usuarios();
            user.setNickname(userName);
            user.setPassword(encript.generatePwd(userPassword));
            UC.createUser(user);
            HttpSession objSesion = request.getSession();
            objSesion.setAttribute("usuario_logueado", user.getNickname());
            objSesion.setAttribute("estado_sesion", EstadoSesion.LOGIN_CORRECTO);
           request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (PreexistingEntityException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ex.getMessage());
        }
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

}
