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
    </head>
    <body>
        <!-- Wrap all page content here -->
        <div id="wrap">
            <header>
                <nav class="navbar navbar-inverse">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <a class="navbar-brand" href="#">Ombues TSIG</a>
                        </div>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
                        </ul>
                    </div>
                </nav>
            </header>
            <div class="col-lg-3">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#pane1" data-toggle="tab">Busqueda</a></li>
                        <li><a href="#pane2" data-toggle="tab">Registrar ombu</a></li>
                        <li><a href="#pane3" data-toggle="tab">Registrar zona de ombues</a></li>
                    </ul>
                    <div class="tab-content">
                        <!-- Informacion de punto -->
                        <div id="pane1" class="tab-pane active">
                            <!-- <button onclick="actualInfo()">Actual Info</button> -->
                            <div>
                                <label>Interaction type:  &nbsp;</label>
                                <label>draw</label>
                                <input type="radio" id="interaction_type_draw" name="interaction_type" value="draw" checked>
                                <label>modify</label>
                                <input type="radio" id="interaction_type_modify" name="interaction_type" value="modify">
                            </div>
                            <div>
                                <label>Geometry type</label>
                                <select id="geom_type">
                                    <option value="Point" selected>Point</option>
                                    <option value="Polygon">Polygon</option>
                                </select>
                            </div>
                            <div>
                                <label>Data type</label>
                                <select id="data_type">
                                    <option value="GeoJSON" selected>GeoJSON</option>
                                    <option value="KML">KML</option>
                                    <option value="GPX">GPX</option>
                                </select>
                            </div>
                            <div id="delete" style="text-decoration:underline;cursor:pointer">
                                Delete all features
                            </div>
                            <label>Data:</label>
                            <textarea id="data" rows="12" style="width:100%"></textarea>
                            <textarea id="geojson-output" placeholder="Resulting WKT will be displayed here"></textarea>
                            <button onclick="convert()">calcular</button>
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
                                        <option>Ombu</option>
                                        <option>Referencia</option>
                                    </select>
                                </div>
                                <button type="button" class="btn btn-default" onclick="registrarOmbu()" >Registrar</button>
                            </div>
                            <div id="myResult"></div>
                        </div>
                    </div>
                </div>
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
        
        <div style="display:none">
            <%@page import="Utils.ConfigManager"%>
            <%@page import="java.util.Properties;" %>
            <% ConfigManager configuracion = new ConfigManager();
                   Properties propiedades = configuracion.getConfigFile("Config.properties");
            %>
            <div id="url">
                <%=propiedades.getProperty("urlGeoserver")%>
            </div>
            <div id="srs">
                <%=propiedades.getProperty("srs")%>
            </div>
        </div>


        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
        <script src="media/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="media/OpenLayers-3.15.1/ol.js" type="text/javascript"></script>
        <script src="media/js/mapa.js" type="text/javascript"></script>

    </body>
</html>