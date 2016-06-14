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
import Entities.ReferenciaOmbu;
import Utils.EstadoSesion;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
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
@WebServlet(name = "GetNoReferencial", urlPatterns = {"/getNoReferencial"})
public class getNoReferencial extends HttpServlet {

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
        Integer categoria=Integer.parseInt(request.getParameter("cat"));
        String nombre=request.getParameter("nombre");
        PuntoOmbuController oDAO = new PuntoOmbuController();
        List<ReferenciaOmbu> ListRO=oDAO.getReferencia(nombre, categoria);
        SimpleDateFormat formatoFecha=new SimpleDateFormat("dd/MM/yyyy");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String cat="";
            if(categoria==0){
                cat="<th>Categoria</th>";
            }
            out.println("<table class=\"table table-bordered\"><tr><th>Nombre</th><th>Descripcion</th>"+cat+"<th>Referencia externa</th></tr>");
            for(ReferenciaOmbu re:ListRO){
                String catOmbu="";
                if(categoria==0){
                    catOmbu="<td>"+re.getCategoriaReferenciasId().getDetalle()+"</td>";
                }
                out.println("<tr><td><a href='#' onclick='getOmbu("+re.getId()+")'>"+re.getOmbues().getNombre()+"</a></td><td>"+re.getOmbues().getDescripcion()+"</td>"+catOmbu+"<td>"+re.getOmbues().getExternalRef()+"</td></tr>");
            }
                  
      /*      for(Comentario coment:ombu.getComentarioList()){
                StringBuffer textBuffer=new StringBuffer(coment.getComentario());
                int loc = (new String(textBuffer).indexOf('\n'));
                while(loc>0){
                    textBuffer.replace(loc, loc+1, "<br>");
                    loc = (new String(textBuffer).indexOf('\n'));
                }
                out.println("<p>"+formatoFecha.format(coment.getFecha())+" "+coment.getIdUser().getNickname()+ "</p><p>"+textBuffer+"</p><br>");
            }
            try {
                if (request.getSession().getAttribute("estado_sesion") == EstadoSesion.LOGIN_CORRECTO) {
                    out.println("<div class=\"form-horizontal\" role=\"form\">\n" +
"                <label>Comentario</label>\n" +
"                <textarea rows=\"10\" class=\"form-control col-sm-10\" form=\"comentario\" id=\"comentario\" ></textarea>\n" +
"            </div><button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\" onclick=\"realizarComentario(ombuId)\" >Enviar</button> ");                    
                }*/
            }catch(Exception e){}
            

        
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
