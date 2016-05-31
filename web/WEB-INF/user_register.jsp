<%-- 
    Document   : user_register
    Created on : 31/05/2016, 06:10:28 PM
    Author     : RedMasPc
--%>
<%@page import="Entities.Usuarios"%>
<%@page import="ControladoresDAO.UsuarioController"%>
<%@page import="Utils.EstadoSesion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Ombues - Registro de usuario</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="media/css/bootstrap.css">
        <link rel="stylesheet" href="media/OpenLayers-3.15.1/ol.css" type="text/css">
        <link rel="stylesheet" href="media/css/main.css">
        <link rel="stylesheet" href="media/font-awesome-4.6.3/css/font-awesome.min.css">
    </head>
    <body  style="background-image: url('media/images/wallpaper.jpg'); background-size: cover; background-repeat: no-repeat; background-position: center center; background-attachment: fixed;">
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
            }
        %>
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>                        
                    </button>
                    <a class="navbar-brand" href="/TSIG">Ombues TSIG</a>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav navbar-right"> 
                      <% if (logeado) {%>
                        <li><a href=""><span class="glyphicon glyphicon-user"></span> <%=user.getNickname()%></a></li>
                        <li><a href="salir"><span class="glyphicon glyphicon-log-in"></span> Salir</a></li>
                      <%   } else { %>
                        <li><a href="/TSIG"><span class="glyphicon glyphicon-tree-deciduous"></span> Ombues</span></a></li>
                      <% }%>
                    </ul>
                </div>
            </div>
        </nav>

        <section id="content" class="section_user_register">

                <div class="page page-forms-common">

                    <!--div class="pageheader">

                        <h2>Ombues <span> Tsig</span></h2>

                        <a href="#" class="btn btn-orange btn-rounded mb-10 right">Ombues</a>

                    </div-->
                     <!-- row -->
                    <div class="row">
                        <!-- col -->
                        <div class="col-md-12">
                        <!-- tile -->
                            <section class="tile tile_user_register">
                              <!-- tile header -->
                                <div class="tile-header dvd dvd-btm">
                                    <h1 class="custom-font"><strong>Nuevo </strong> Usuario</h1>
                                </div>
                                <!-- /tile header -->
                                <!-- tile body -->
                                <div class="tile-body">

                                    <form name='form' class="form-horizontal" role="form" action="UserRegister" method="post">

                                        <div class="form-group col-md-12 legend"> <h4><strong>Informacion</strong> Basica</h4> <p></p> </div><br><br><br>

                                        <div class="form-group">
                                            <label for="userName" class="col-sm-2 control-label">* Nombre</label>
                                            <div class="col-sm-10">
                                                <input type="text" name="userName" class="form-control" id="userName" required>
                                                <span class="help-block">Nombre de usuario es requerido</span>
                                            </div>
                                        </div>
                                        
                                        <hr class="line-dashed line-full"/>

                                        <div class="form-group">
                                            <label for="contactName" class="col-sm-2 control-label">* Contacto</label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" name="contactName" id="contactName" required>
                                                <span class="help-block">Nombre de Contacto es requerido</span>
                                            </div>
                                        </div>

                                        <hr class="line-dashed line-full"/>

                                        <div class="form-group">
                                        
                                          <label for="emailAddress" class="col-sm-2 control-label">* Email</label>
                                            <div class="col-sm-10">
                                                <input type="email" class="form-control" name="emailAddress" id="emailAddress" required>
                                                <span class="help-block">Email es requerido</span>
                                                <!--<span class="help-block">Not valid Email</span>-->
                                            </div>
                                        </div>
                                        
                                        
                                        <div class="form-group col-md-12 legend"> <h4><strong>Seguridad </strong> Contraseña</h4> <p></p> </div><br><br><br>

                                        <div class="form-group">
                                            <label for="password" class="col-sm-2 control-label">* Contraseña</label>
                                            <div class="col-sm-10">
                                                <input type="password" class="form-control" name="password" id="password" required>
                                                <span class="help-block">Contraseña es requerida</span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="passwordConfirm" class="col-sm-2 control-label">* Confirmacion</label>
                                            <div class="col-sm-10">
                                                <input type="password" class="form-control" name="passwordConfirm" id="passwordConfirm" required>
                                                <span class="help-block">Requerido</span>
                                            </div>
                                        </div>


                                        <div class="form-group col-md-12 legend"> <h4><strong>Extra</strong></h4> <p></p> </div><br><br><br>

                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"></label>
                                            <div class="col-sm-10">

                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" value="" name="allowLocation" id="allowLocation" >
                                                        Permitir conocer mi ubicacion
                                                    </label>
                                                </div>
                                            </div>
                                        </div>

                                        <!--<hr class="line-dashed line-full"/>


                                        <div class="form-group">
                                            <label for="message" class="col-sm-2 control-label"> Comments </label>
                                            <div class="col-sm-10">
                                                <textarea class="form-control" rows="5" name="message" id="message" placeholder="Write your comments..."></textarea>
                                            </div>
                                        </div>

                                        <hr class="line-dashed line-full"/>

                                        <div class="form-group">
                                            <label for="input04" class="col-sm-2 control-label">External Reference</label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" id="input04" ng-model="vm.adv.externalref">
                                            </div>
                                        </div>-->

                                        <hr class="line-dashed line-full"/>

                                        <div class="form-group">
                                            <div class="col-sm-4 col-sm-offset-2">
                                                <a href="/TSIG" class="btn btn-lightred">Cancelar</a>
                                                <input type="submit" class="btn btn-default" value="Alla Vamos!" />
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <!-- /tile body -->
                            </section>
                            <!-- /tile -->
                        </div>
                        <!-- /col -->
                    </div>
                    <!-- /row -->
               </div>
        </section>
<!--/ CONTENT -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
        <script src="media/js/bootstrap.min.js" type="text/javascript"></script>
    </body>
</html>