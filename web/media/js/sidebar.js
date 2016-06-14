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
