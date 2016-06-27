/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ControladoresDAO.PuntoOmbuController;
import DAO.OmbuesJpaController;
import Entities.Comentario;
import Entities.Ombues;
import Utils.EstadoSesion;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Diego
 */
@WebServlet(name = "GetOmbu", urlPatterns = {"/getombu"})
public class GetOmbu extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PuntoOmbuController oDAO = new PuntoOmbuController();
        Ombues ombu = oDAO.getOmbuxId(Integer.parseInt(request.getParameter("id")));
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String dato = "";
            if (ombu.getReferenciaOmbu() != null) {
                dato = "<td>Referencia: </td><td>" + ombu.getExternalRef() + "</td>";
            } else {
                dato = "<td>Direccion: </td><td>" + ombu.getDireccion() + "</td>";
            }
            out.println("<table><tr><td>Nombre: </td><td>" + ombu.getNombre() + "</td></tr><tr><td>Descripcion:&nbsp;&nbsp;</td><td>" + ombu.getDescripcion() + "</td></tr><tr>" + dato + "</tr></table><hr>");
            int counter = 0;
            String activo="active";
            Collection<Entities.Imagenes> imagenes = ombu.getImagenesCollection();
            out.println("<div id=\"myCarousel\" class=\"carousel slide\" data-ride=\"carousel\"><div class=\"carousel-inner\" role=\"listbox\">");
            for (Entities.Imagenes img : imagenes) {   

                out.println("<div class=\"item "+ activo +"\"><img class='' alt='Image not found' width=\"380\" height=\"380\" src='images/" + img.getNombre() + "'></div>");
                activo="";
                counter ++;
            }
            if(counter != 0){
            out.println("</div>  <a class=\"left carousel-control\" href=\"#myCarousel\" role=\"button\" data-slide=\"prev\">\n" +
"    <span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>\n" +
"    <span class=\"sr-only\">Previous</span>\n" +
"  </a>\n" +
"  <a class=\"right carousel-control\" href=\"#myCarousel\" role=\"button\" data-slide=\"next\">\n" +
"    <span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>\n" +
"    <span class=\"sr-only\">Next</span>\n" +
"  </a></div>"); //Fin Div para Imagenes}
            }
            out.println("<div id=\"myComments\">");
            for (Comentario coment : ombu.getComentarioList()) {
                StringBuffer textBuffer = new StringBuffer(coment.getComentario());
                int loc = (new String(textBuffer).indexOf('\n'));
                while (loc > 0) {
                    textBuffer.replace(loc, loc + 1, "<br>");
                    loc = (new String(textBuffer).indexOf('\n'));
                }
                out.println("<div style=\"background-color: #F8F8F8; padding: 4px; border-radious: 2px;\"><p>" + formatoFecha.format(coment.getFecha()) + " <strong>" + coment.getIdUser().getNickname() + "</strong> dijo: </p><p>" + textBuffer + "</p></div><br>");
            }
            out.println("</div>");
            try {
                if (request.getSession().getAttribute("estado_sesion") == EstadoSesion.LOGIN_CORRECTO) {
                    out.println("<div class=\"form-horizontal\" role=\"form\">\n"
                            + "<div class=\"row\"><label class=\"col-sm-12\">Nuevo comentario</label>\n"
                            + "<div class=\"col-sm-12\"><textarea rows=\"3\" class=\"form-control\" form=\"comentario\" id=\"comentario\" style=\"max-width: 400px; margin-bottom: 5px;\"></textarea></div></div></div>\n"
                            + "<button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\" onclick=\"realizarComentario(ombuId)\" >Enviar</button> ");
                }
            } catch (Exception e) {
            }

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
