/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ControladoresDAO.UsuarioController;
import Entities.Ombues;
import Entities.ReferenciaOmbu;
import Entities.Usuarios;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Galvadion
 */
@WebServlet(name = "InsertRefOmbu", urlPatterns = {"/Puntos/InsertRefOmbu"})
public class InsertRefOmbu extends HttpServlet {

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
            out.println("<title>Servlet InsertPuntoOmbu</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InsertPuntoOmbu at " + request.getContextPath() + "</h1>");
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
        try {
            UsuarioController UC = new UsuarioController();
            Usuarios user = UC.getUserXNick(request.getSession().getAttribute("usuario_logueado").toString());
            ControladoresDAO.PuntoOmbuController PuC = new ControladoresDAO.PuntoOmbuController();
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String direccion = request.getParameter("referencia");
            String quees = request.getParameter("quees");
            Ombues ombu = new Ombues();
            ombu.setNombre(nombre);
            ombu.setDescripcion(descripcion);
            ombu.setExternalRef(direccion);
            ombu.setIdUsuario(user);
            Integer categoriaRefId=Integer.parseInt(quees);
            Integer ombuId=PuC.crearPuntoOmbu(ombu);
            ReferenciaOmbu ref=new ReferenciaOmbu();
            ref.setOmbues(PuC.getOmbuxId(ombuId));
            ref.setCategoriaReferenciasId(PuC.getCategoriaRefxId(categoriaRefId));
            PuC.crearReferenciaOmbu(ref);
            System.out.println("Falla algo aca");
        } catch (Exception ex) {
            System.out.println("Falla algo en excepcion");
            Logger.getLogger(InsertRefOmbu.class.getName()).log(Level.SEVERE, null, ex);
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
