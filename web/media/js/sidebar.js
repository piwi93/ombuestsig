var side_bar_flag = false;

function expand_bar() {
    if(side_bar_flag){
        document.getElementById("side-bar").style.left = "100%";
        document.getElementById("expand-button-icon").className = "glyphicon glyphicon-resize-full";
        side_bar_flag = false;
    }
    else{
        document.getElementById("side-bar").style.left = "calc(100% - 315px)";
        document.getElementById("expand-button-icon").className = "glyphicon glyphicon-resize-small";
        side_bar_flag = true;
    }
}

$(document).ready(function(){
    
});

function resize() {
    var map = document.getElementById("map");
    var sidebar = document.getElementById("side-bar");
    var valHeight = window.innerHeight - 110;
    map.style.height = valHeight + 'px';
    sidebar.style.height = valHeight + 'px';
}

window.onload = function (event) {
    resize();
}


//onchange naturalWidth 
window.onresize = function(event) {
    resize();
}