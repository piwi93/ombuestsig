var side_bar_flag = false;

function expand_bar() {
    var valWidth = window.innerWidth;
    if(side_bar_flag){
        document.getElementById("side-bar").style.left = "100%";
        document.getElementById("expand-button-icon").className = "glyphicon glyphicon-resize-full";
        side_bar_flag = false;
    }
    else{
        if(valWidth < 360){
            document.getElementById("side-bar").style.left = "calc(100% - 240px)";
        }
        else{
            document.getElementById("side-bar").style.left = "calc(100% - 315px)";
        }
        document.getElementById("expand-button-icon").className = "glyphicon glyphicon-resize-small";
        side_bar_flag = true;
    }
}

function resize() {
    var map = document.getElementById("map");
    var sidebar = document.getElementById("side-bar");
    var valHeight = window.innerHeight - 110;
    map.style.height = valHeight + 'px';
    sidebar.style.height = valHeight + 'px';
    var valWidth = window.innerWidth;
    if(side_bar_flag && valWidth < 360){
        document.getElementById("side-bar").style.left = "calc(100% - 240px)";
    }
    else if(side_bar_flag && valWidth > 360){
        document.getElementById("side-bar").style.left = "calc(100% - 315px)";
    }
}

window.onload = function (event) {
    resize();
}

window.onresize = function(event) {
    resize();
}

var flagOmbu = false;
var flagZona = false;
var flagReferencia = false;

function registro(type) {
    var registroOmbu = document.getElementById("registroOmbu");
    var registroZona = document.getElementById("registroZona");
    var registroReferencia = document.getElementById("registroReferencia");
    registroOmbu.style.backgroundColor = "#fff";
    registroZona.style.backgroundColor = "#fff";
    registroReferencia.style.backgroundColor = "#fff";
    
    if(type === "registroOmbu"){
        if(!flagOmbu){
            registroOmbu.style.backgroundColor = "#f2f3f4";
            flagOmbu = true;
        }
        else{
            registroOmbu.style.backgroundColor = "#fff";
            flagOmbu = false;
        }
    }
    if(type === "registroZona"){
        if(!flagZona){
            registroZona.style.backgroundColor = "#f2f3f4";
            flagZona = true;
        }
        else{
            registroZona.style.backgroundColor = "#fff";
            flagZona = false;
        }
    }
    if(type === "registroReferencia"){
        if(!flagReferencia){
            registroReferencia.style.backgroundColor = "#f2f3f4";
            flagReferencia = true;
        }
        else{
            registroReferencia.style.backgroundColor = "#fff";
            flagReferencia = false;
        }
    }
}