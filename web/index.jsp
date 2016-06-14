<%@page import="Entities.CategoriaReferencias"%>
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
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <link rel="stylesheet" href="media/css/bootstrap.css">
        <link rel="stylesheet" href="media/OpenLayers-3.15.1/ol.css" type="text/css">
        <link rel="stylesheet" href="media/css/main.css">
        <link rel="stylesheet" href="media/font-awesome-4.6.3/css/font-awesome.min.css">
        
        <link rel="stylesheet" href="media/js/dropzone/basic.css">
        <link rel="stylesheet" href="media/js/dropzone/dropzone.css">
        
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
            <div class="row">
                <header>
                    <nav class="navbar navbar-inverse no-border-radius">
                        <div class="container-fluid">
                            <div class="navbar-header">
                                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>                        
                                </button>
                                <a class="navbar-brand" href="">Ombues TSIG</a>
                            </div>
                            <div class="collapse navbar-collapse" id="myNavbar">
                                <ul class="nav navbar-nav navbar-right"> 
                                    <% if (logeado) {%>
                                    <li><a href="#"><span class="glyphicon glyphicon-user"></span> <%=user.getNickname()%></a></li>
                                    <li><a href="salir"><span class="glyphicon glyphicon-log-in"></span> Salir</a></li>
                                        <%   } else { %>
                                    <li><a href="userRegister"><span class="glyphicon glyphicon-user"></span> Sign Up</span></a></li>
                                    <li><a href="#modalLogIn" data-toggle="modal"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
                                        <% } %>
                                </ul>
                            </div>
                        </div>
                    </nav>
                </header>
            </div>
            
            <div class="row">
                
                <div class="col-lg-12" id="map">
                    <div id="popup" class="ol-popup">
                        <a href="#" id="popup-closer" class="ol-popup-closer"></a>
                        <div id="popup"></div>
                    </div>
                </div>

                <div class="col-lg-3" id="side-bar">
                    <div id="expand-button-container">
                        <button class="btn btn-success" id="expand-button" onclick="expand_bar()">
                            <span class="glyphicon glyphicon-resize-full" id="expand-button-icon" />
                        </button>
                    </div>
                    <div class="tabbable">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#pane1" data-toggle="tab">Busqueda</a></li>
                            <li><a href="#pane5" data-toggle="tab" id="regzona">Statics</a></li>
                                <% if (logeado) { %>
                            <li><a href="#pane2" data-toggle="tab" id="regpunto">Ombu</a></li>
                            <li><a href="#pane3" data-toggle="tab" id="regzona">Zona</a></li>
                            <li><a href="#pane4" data-toggle="tab" id="regzona">Referencia a ombu</a></li>
                                <% } %>
                        </ul>
                        <div class="tab-content" style="min-height: 100% !important;">
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
                                        <label  for="text">Dirección:</label>
                                        <input type="text" class="form-control" id='direccion' placeholder="Ingrese una dirección para el ombu">
                                    </div>
                                    <div class="form-group" style="display:none">
                                        <label  for="text">Ubicación:</label>
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
                                    <div class="form-group">
                                            <div id="picDropzone" style="height: 150px; width: 100%;
                                              border:dashed; color:blue; border-color: skyblue"></div>
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
                                        <label  for="text">Dirección:</label>
                                        <input type="text" class="form-control" id='direccion' placeholder="Ingrese una descripcion para el ombu">
                                    </div>
                                    <div class="form-group">
                                        <label  for="text">Ubicación:</label>
                                        <input type="text" class="form-control" id='ubicacion' placeholder="Ingrese una descripcion para el ombu">
                                    </div>
                                    <button type="button" class="btn btn-default" onclick="registrarZonaOmbu()" >Registrar</button>
                                </div>
                                <div id="myResult"></div>
                            </div>
                            
                            <!-- Registrar referencia -->
                            <div id="pane4" class="tab-pane">
                                <div class="form-vertical" role="form">
                                    <div class="form-group">
                                        <label  for="text">Nombre:</label>
                                        <input type="text" class="form-control" id='refnombre' placeholder="Ingrese el nombre del ombu">
                                    </div>
                                    <div class="form-group">
                                        <label  for="text">Descripcion:</label>
                                        <input type="text" class="form-control" id='refdescripcion' placeholder="Ingrese una descripcion para la referencia">
                                    </div>
                                    <div class="form-group">
                                        <label  for="text">Referencia:</label>
                                        <input type="text" class="form-control" id='refRef' placeholder="Ingrese un enlace a algo que referencie al ombu">
                                    </div>
                                    <div class="form-group">
                                        <label  for="text">Que es?</label>
                                        <select class="form-control" id="refcategoria">
                                            <%   
                                                for (CategoriaReferencias cat : PoC.categoriaRefList()) {
                                            %>
                                            <option value="<%=cat.getId()%>"><%=cat.getDetalle()%></option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <button type="button" class="btn btn-default" onclick="registrarRefOmbu()" >Registrar</button>
                                </div>
                                <div id="myResult"></div>
                            </div>
                                        
                            <!-- Stats -->
                            <div id="pane5" class="tab-pane">
                                
                            </div>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- Wrap Div end -->

        <footer id="footer">
            <div class="container">
                <p class="text-muted credit" style="color: #9d9d9d">Copyright Grupo 13</p>
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
        
        <script src="media/js/sidebar.js" type="text/javascript"></script>
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
        <script src="media/js/dropzone/dropzone.js"></script>
        <script src="media/js/imagenes.js"></script>
        
        <script src="media/js/mapa.js" type="text/javascript"></script>
        
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
                                <label class="control-label col-sm-4" for="text">Contraseña:</label>
                                <div class="col-sm-8">
                                    <INPUT type="password" class="form-control" id="cuadroTxtLogin" name="txtPwd" value="" size="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10"><INPUT type='submit' id='btnLogin' class='btn btn-primary' name='btnLogIn' Value='Iniciar sesión' onclick='Submit()'/></div>
                            </div>

                            <%
                                session = request.getSession();
                                if (session.getAttribute("estado_sesion") == EstadoSesion.LOGIN_INCORRECTO) {
                                    session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
                            %><p style='font-size:11px; display:inline-block'>Usuario o contraseña incorrecta.</p>
                            <%
                                   }%>
                        </form>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        
        <!-- DropZone preview Template -->
        <div id="preview-template" style="display: none;">
            <div class="dz-preview dz-file-preview col-xs-3" style="height: 75px">
                <div class="dz-details">
                  <!--<div class="dz-filename"><span data-dz-name></span></div>-->
                  <!--<div class="dz-size" data-dz-size></div>-->
                  <img data-dz-thumbnail style="max-height: 70px; max-width: 100%" />
                </div>
                <div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span></div>
                <div class="dz-success-mark"><span></span></div>
                <div class="dz-error-mark"><span></span></div>
                <div class="dz-error-message"><span data-dz-errormessage></span></div>
            </div>
        </div>
        <!-- END DropZone preview Template -->
    </body>
</html>