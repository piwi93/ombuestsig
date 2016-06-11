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
        <title>Ombues</title>
        <meta charset="UTF-8">
        <link rel="shortcut icon" type="image/x-icon" href="media/images/icon.png" />
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <link rel="stylesheet" href="media/css/bootstrap.css">
        <link rel="stylesheet" href="media/OpenLayers-3.15.1/ol.css" type="text/css">
        <link rel="stylesheet" href="media/css/main.css">
        <link rel="stylesheet" href="media/font-awesome-4.6.3/css/font-awesome.min.css">
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
                                <a class="navbar-brand custom-font" href=""><strong style="margin-left: 5px;">Ombues </strong> TSIG</a>
                            </div>
                            <div class="collapse navbar-collapse" id="myNavbar">
                                <ul class="nav navbar-nav navbar-right"> 
                                    <% if (logeado) {%>
                                    <li><a href="#"><span class="glyphicon glyphicon-user"></span> <%=user.getNickname()%></a></li>
                                    <li><a href="salir"><span class="glyphicon glyphicon-log-in"></span> Salir</a></li>
                                        <%   } else { %>
                                    <li><a href="userRegister"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
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
                                <% if (logeado) { %>
                            <li><a href="#pane2" data-toggle="tab" id="regpunto">Ombu</a></li>
                            <li><a href="#pane3" data-toggle="tab" id="regzona">Zona</a></li>
                            <li><a href="#pane4" data-toggle="tab" id="regzona">Ref.Ombu</a></li>
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
                                        <input type="text" class="form-control" id='direccion' placeholder="Ingrese una descripcion para el ombu">
                                    </div>
                                    <div class="form-group">
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
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- Wrap Div end -->

        <footer id="footer">
            <div class="container">
                <p class="text-muted credit custom-font" style="color: #9d9d9d"><strong>Copyright</strong> Grupo 13</p>
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
        <script src="media/js/sidebar.js" type="text/javascript"></script>
        
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
                        <h4 class="modal-title custom-font"><strong>Log </strong> In</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form"  action="iniciar-sesion" method="POST">
                            
                            <div class="form-group">
                                <label for="txtNick" class="col-sm-3 control-label">* Usuario</label>
                                <div class="col-sm-8">
                                    <INPUT type="text" class="form-control" id="txtNick" name="txtNick" value="" size="50" required />
                                    <span class="help-block">Nombre de usuario es requerido</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="txtPwd" class="col-sm-3 control-label">* Contraseña</label>
                                <div class="col-sm-8">
                                    <INPUT type="password" class="form-control" id="txtPwd" name="txtPwd" value="" size="50" required />
                                    <span class="help-block">Contraseña es requerida</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-12">
                                    <button type="button" class="btn btn-lightred" data-dismiss="modal">Cancelar</button>
                                    <INPUT type='submit' id='btnLogin' class="btn btn-default" name='btnLogIn' Value='Iniciar sesión' onclick='Submit()'/>
                                </div>
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
    </body>
</html>