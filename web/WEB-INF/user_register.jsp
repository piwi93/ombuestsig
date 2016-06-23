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
        <title>Ombues - Registro</title>
        <meta charset="UTF-8">
        <link rel="shortcut icon" type="image/x-icon" href="media/images/icon.png" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="media/css/bootstrap.css">
        <link rel="stylesheet" href="media/OpenLayers-3.15.1/ol.css" type="text/css">
        <link rel="stylesheet" href="media/css/main.css">
        <link rel="stylesheet" href="media/font-awesome-4.6.3/css/font-awesome.min.css">
    </head>
    <body  style="background-image: url('media/images/wallpaper.jpg'); background-size: cover; background-repeat: no-repeat; background-position: center center; background-attachment: fixed; overflow-y: auto !important;">
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

        <div id="wrap">
            <div class="row">
                <nav class="navbar navbar-inverse no-border-radius">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>                        
                            </button>
                            <a class="navbar-brand custom-font" href="/TSIG"><strong style="margin-left: 5px;">Ombues </strong> TSIG</a>
                        </div>
                        <div class="collapse navbar-collapse" id="myNavbar">
                            <ul class="nav navbar-nav navbar-right"> 
                                <li><a href="/TSIG"><span class="glyphicon glyphicon-tree-deciduous"></span> Ombues</a></li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>

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

                                    <form name='form' class="form-horizontal" role="form" id="UserRegister" action="UserRegister" method="post">

                                        <div class="form-group col-md-12 legend"> <h4><strong>Informacion</strong> Basica</h4> <p></p> </div><br><br><br>

                                        <div class="form-group"id="nickName">
                                            <label for="userName" class="col-sm-2 control-label">* Nombre</label>
                                            <div class="col-sm-10" >
                                                <input type="text" name="userName" class="form-control" id="userName" required>
                                                <span class="help-block" id="nickNameSpan">Nombre de usuario es requerido</span>
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

                                        <div class="form-group" id="pass">
                                            <label for="password" class="col-sm-2 control-label">* Contraseña</label>
                                            <div class="col-sm-10">
                                                <input type="password" class="form-control" name="password" id="password" required>
                                                <span class="help-block">Contraseña es requerida</span>
                                            </div>
                                        </div>

                                        <div class="form-group" id="pass2">
                                            <label for="passwordConfirm" class="col-sm-2 control-label">* Confirmacion</label>
                                            <div class="col-sm-10">
                                                <input type="password" class="form-control" name="passwordConfirm" id="passwordConfirm" required>
                                                <span class="help-block" id="contrasenia">Requerido</span>
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
                                                <input type="submit" class="btn btn-default" id="UserRegister" value="Alla Vamos!" />
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
        </div>

        <!--/ CONTENT -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.3/jquery.min.js"></script>
        <script src="media/js/bootstrap.min.js" type="text/javascript"></script>
        <script>
            $(document).on("submit", "#UserRegister", function (event) {
                event.preventDefault();
                var nombre = document.getElementById("userName").value;
                var mail = document.getElementById("emailAddress").value;
                var contraseña = document.getElementById("password").value;
                var confirmacion = document.getElementById("passwordConfirm").value;
                if (contraseña == confirmacion) {
                    $("#pass").removeClass("alert alert-danger");
                    $("#pass2").removeClass("alert alert-danger");
                    $("#contrasenia").removeClass("alert alert-danger");
                    $.post("userRegister", {
                        userName: nombre, emailAddress: mail, password: contraseña
                    }, function (responseText) {

                    }).fail(function (responseText) {

                        document.getElementById("nickNameSpan").innerHTML = "Ya hay un usuario con ese nombre";
                        $("#nickName").addClass("alert alert-danger");
                        $("#nickNameSpan").addClass("alert alert-danger");
                    });
                } else {
                    document.getElementById("contrasenia").innerHTML = "La contraseña no coincide";
                    $("#pass").addClass("alert alert-danger");
                    $("#pass2").addClass("alert alert-danger");
                    $("#contrasenia").addClass("alert alert-danger");
                }


            })
        </script>
    </body>
</html>