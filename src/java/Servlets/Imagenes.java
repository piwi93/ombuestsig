/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Utils.ConfigManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

/**
 *
 * @author Nicolás Aquino <nicoaquin@hotmail.com>
 */
public class Imagenes extends HttpServlet {

   private boolean isMultipart;
   private String filePath, tempPath;
   private int maxFileSize = 5000 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;
   
   public void init( ){
      // Get the file location where it would be stored.
      filePath = System.getProperty("user.home")+System.getProperty("file.separator")+"TSIG_Imagenes"; 
      File directorio = new File(filePath);
      if(!directorio.exists())
      {
          directorio.mkdir();
      }
      tempPath = filePath + System.getProperty("file.separator") + "temp";
      File temp =  new File(tempPath);
      if(!temp.exists())
      {
          temp.mkdir();
      }
   }
    
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
            out.println("<title>Servlet Imagenes</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Imagenes at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("application/json");
        java.io.PrintWriter out = response.getWriter( );
        if( !isMultipart ){
           out.println("<p>No file uploaded</p>"); 
           return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File(tempPath));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try{ 
          // Parse the request to get file items.
          List fileItems = upload.parseRequest(request);

          // Process the uploaded file items
          Iterator i = fileItems.iterator();
          
          ConfigManager configuracion = new ConfigManager();
          Properties propiedades = configuracion.getConfigFile("Config.properties");

          String respuesta = "{'files': [";
          int j = 0;
          while ( i.hasNext () ) 
          {
             FileItem fi = (FileItem)i.next();
             if ( !fi.isFormField () )	
             {
                // Get the uploaded file parameters
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();
                String contentType = fi.getContentType();
                boolean isInMemory = fi.isInMemory();
                long sizeInBytes = fi.getSize();
                // Write the file
                if( fileName.lastIndexOf("\\") >= 0 ){
                   file = new File( filePath + System.getProperty("file.separator") +
                           new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()) + "_" +
                           fileName.substring( fileName.lastIndexOf("\\"))) ;
                }else{
                   file = new File( filePath + System.getProperty("file.separator") +
                           new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()) + "_"+
                           fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                }
                fi.write( file ) ;
                if(j != 0)
                    respuesta += ",";
                respuesta += "{'name':" + file.getName() + ", 'size': " + sizeInBytes + ", " +
                        "'url': " + propiedades.getProperty("http://localhost:8080/" + "TSIG_Imagenes" + ", ") +
                        "'thumbnailUrl': ,'deleteUrl': , 'deleteType': 'DELETE'}" ;
             }
             j++;
          }
          respuesta += "]}";
          out.println(respuesta);
        }catch(Exception ex) {
            System.out.println(ex);
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
