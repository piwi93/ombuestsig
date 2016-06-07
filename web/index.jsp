<%@page import="Entities.Usuarios"%>
<%@page import="ControladoresDAO.UsuarioController"%>
<%@page import="Utils.EstadoSesion"%>
<%@page import="ControladoresDAO.PuntoOmbuController"%>
<%@page import="Entities.Categoria"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>OMBUES</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="media/css/bootstrap.css">
        <link rel="stylesheet" href="media/OpenLayers-3.15.1/ol.css" type="text/css">
        <link rel="stylesheet" href="media/css/main.css">
        <link rel="stylesheet" href="media/font-awesome-4.6.3/css/font-awesome.min.css">
        <!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
        <link rel="stylesheet" href="media/css/jquery.fileupload.css">
        <link rel="stylesheet" href="media/css/jquery.fileupload-ui.css">
        <!-- CSS adjustments for browsers with JavaScript disabled -->
        <noscript><link rel="stylesheet" href="media/css/jquery.fileupload-noscript.css"></noscript>
        <noscript><link rel="stylesheet" href="media/css/jquery.fileupload-ui-noscript.css"></noscript>
    </head>
    <body>
        <!-- Wrap all page content here -->
        <%
            session = request.getSession();
            Usuarios user = null;
            boolean logeado = false;
            try {
                if (session.getAttribute("estado_sesion") == EstadoSesion.LOGIN_CORRECTO) {
                    UsuarioController UC = new UsuarioController();
                    user = UC.getUserXNick(request.getSession().getAttribute("usuario_logueado").toString());
                    logeado = true;
                }
            } catch (Exception e) {
            }%>
        <div id="wrap">
            <header>
                <nav class="navbar navbar-inverse custom_bar">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <a class="navbar-brand" href="#">Ombues TSIG</a>
                        </div>
                        <ul class="nav navbar-nav navbar-right"> 
                            <% if (logeado) {%>
                            <li><a href="#"><span class="glyphicon glyphicon-log-in"></span><span class="nav_hide"><%=user.getNickname()%> </span></a></li>
                            <li><a href="salir"><span class="glyphicon glyphicon-log-in"></span><span class="nav_hide">Salir</span></a></li>
                                        <%   } else { %>
                            <li><a href="userRegister"><span class="glyphicon glyphicon-user"></span><span class="nav_hide"> Sign Up</span></a></li>
                            <li><a href="#modalLogIn" data-toggle="modal"><span class="glyphicon glyphicon-log-in"></span><span class="nav_hide"> Login</span></a></li>
                                        <% }%>
                        </ul>
                    </div>
                </nav>
            </header>
            <div class="col-lg-3">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#pane1" data-toggle="tab">Busqueda</a></li>
                            <% if (logeado) { %>
                        <li><a href="#pane2" data-toggle="tab" id="regpunto">Registrar ombu</a></li>
                        <li><a href="#pane3" data-toggle="tab" id="regzona">Registrar zona de ombues</a></li>
                            <% } %>
                    </ul>
                    <div class="tab-content">
                        <!-- Informacion de punto -->
                        <div id="pane1" class="tab-pane active">
                            <button onclick="getLocation()">Actual Info</button> 
                            <div>
                                <label>Interaction type:  &nbsp;</label>
                                <label>draw</label>
                                <input type="radio" id="interaction_type_draw" name="interaction_type" value="draw" checked>
                                <label>modify</label>
                                <input type="radio" id="interaction_type_modify" name="interaction_type" value="modify">
                            </div>

                        </div>
                        <!-- Registrar ombu -->
                        <div id="pane2" class="tab-pane">
                            <div class="form-vertical" role="form">
                                <div class="form-group">
                                    <label  for="text">Nombre:</label>
                                    <input type="text" class="form-control" id='nombre' placeholder="Ingrese el nombre del ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Descripcion:</label>
                                    <input type="text" class="form-control" id='descripcion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Direcci�n:</label>
                                    <input type="text" class="form-control" id='direccion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Ubicaci�n:</label>
                                    <input type="text" class="form-control" id='ubicacion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Que es?</label>
                                    <select class="form-control" id="categoria">
                                        <%
                                            PuntoOmbuController PoC = new PuntoOmbuController();
                                            for (Categoria cat : PoC.categoriasList()) {
                                        %>
                                        <option value="<%=cat.getId()%>"><%=cat.getNombre()%></option>
                                        <% } %>
                                    </select>
                                </div>
                                <button type="button" class="btn btn-default" onclick="registrarOmbu()" >Registrar</button>
                            </div>
                            <div id="myResult"></div>
                        </div>
                        <!-- Registrar zona ombu -->
                        <div id="pane3" class="tab-pane">
                            <div class="form-vertical" role="form">
                                <div class="form-group">
                                    <label  for="text">Nombre:</label>
                                    <input type="text" class="form-control" id='nombre' placeholder="Ingrese el nombre del ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Descripcion:</label>
                                    <input type="text" class="form-control" id='descripcion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Direcci�n:</label>
                                    <input type="text" class="form-control" id='direccion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <div class="form-group">
                                    <label  for="text">Ubicaci�n:</label>
                                    <input type="text" class="form-control" id='ubicacion' placeholder="Ingrese una descripcion para el ombu">
                                </div>
                                <button type="button" class="btn btn-default" onclick="registrarZonaOmbu()" >Registrar</button>
                            </div>
                            <div id="myResult"></div>
                        </div>
                    </div>
                </div>
                                    
                <!-- Upload Image TEST -->
                <div id="imagesForm">
                    <input id="fileupload" type="file" name="files[]" method="POST" enctype="multipart/form-data" multiple>
                    <div id="progress">
                        <div class="bar" style="width: 0%;"></div>
                    </div>
                </div>
                
                <!-- FIN Upload Image TEST -->
                    
            </div>

            <div class="col-lg-9">
                <div style="width: 100%; height: 150%" id="map">
                    <div id="popup" class="ol-popup">
                        <a href="#" id="popup-closer" class="ol-popup-closer"></a>
                        <div id="popup"></div>
                    </div>
                </div>
            </div>

        </div><!-- Wrap Div end -->
        <footer id="footer">
            <div class="container">
                <p class="text-muted credit">Copyright Grupo 13</p>
            </div>
        </footer>

        <div style="display: none">
            <%@page import="Utils.ConfigManager"%>
            <%@page import="java.util.Properties" %>
            <%
                ConfigManager configuracion = new ConfigManager();
                Properties propiedades = configuracion.getConfigFile("Config.properties");
            %>
            <div id="url_wfs">
                <%=propiedades.getProperty("urlGeoserverWFS")%>
            </div>
            <div id="url_wms">
                <%=propiedades.getProperty("urlGeoserverWMS")%>
            </div>
            <div id="srs">
                <%=propiedades.getProperty("srs")%>
            </div>
        </div>


        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
        <script src="media/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="media/OpenLayers-3.15.1/ol.js" type="text/javascript"></script>
        <script src="media/js/mapa.js" type="text/javascript"></script>
        
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
        <script src="media/js/jQuery-File-Uploader/vendor/jquery.ui.widget.js"></script>
        <!-- The Templates plugin is included to render the upload/download listings -->
        <!--<script src="//blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script>-->
        <!-- The Load Image plugin is included for the preview images and image resizing functionality -->
        <script src="//blueimp.github.io/JavaScript-Load-Image/js/load-image.all.min.js"></script>
        <!-- The Canvas to Blob plugin is included for image resizing functionality -->
        <script src="//blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
        <!-- blueimp Gallery script -->
        <script src="//blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
        <!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
        <script src="media/js/jQuery-File-Uploader/jquery.iframe-transport.js"></script>
        <!-- The basic File Upload plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload.js"></script>
        <!-- The File Upload processing plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-process.js"></script>
        <!-- The File Upload image preview & resize plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-image.js"></script>
        <!-- The File Upload audio preview plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-audio.js"></script>
        <!-- The File Upload video preview plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-video.js"></script>
        <!-- The File Upload validation plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-validate.js"></script>
        <!-- The File Upload user interface plugin -->
        <script src="media/js/jQuery-File-Uploader/jquery.fileupload-ui.js"></script>
        <!-- The main application script -->
        <script src="media/js/jQuery-File-Uploader/main.js"></script>
        
        <div class="modal fade" id="modalInfo" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Informacion del ombu</h4>
                    </div>
                    <div class="modal-body" id="info-body">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                    </div>
                </div>  
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->    
            <div class="modal fade" id="modalLogIn" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Log In</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form"  action="iniciar-sesion" method="POST">
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="text">Usuario:</label>
                        <div class="col-sm-8">
                            <INPUT type="text" class="form-control" id="cuadroTxtLogin" name="txtNick" value="" size="50"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="text">Contrase�a:</label>
                        <div class="col-sm-8">
                            <INPUT type="password" class="form-control" id="cuadroTxtLogin" name="txtPwd" value="" size="50"/>
                        </div>
                    </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10"><INPUT type='submit' id='btnLogin' class='btn btn-primary' name='btnLogIn' Value='Iniciar sesi�n' onclick='Submit()'/></div>
                        </div>
                    
                    <%
                        session =request.getSession();
                        if(session.getAttribute("estado_sesion") == EstadoSesion.LOGIN_INCORRECTO){
                            session.setAttribute("estado_sesion",EstadoSesion.NO_LOGIN);
                            %><p style='font-size:11px; display:inline-block'>Usuario o contrase�a incorrecta.</p>
                           <%  
                        }%>
                </form>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>  
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</body>
</html>