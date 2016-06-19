/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * 
 * Se definen las variables globales a utilizar en el sistema
 */
var bounds,
        map,
        view,
        latitud,
        longitud,
        ombuId, lonlat, lon, lat, lon3857, lat3857, dist, dife,
        // make interactions global so they can later be removed
        select_interaction,
        draw_interaction,
        modify_interaction,
        urlGeoserverWFS,
        urlGeoserverWMS,
        SRS, cantCat,
        wms_punto,
        vector_layer, formatWFS, formatGMLPunto, formatGMLZona
content = document.getElementById('popup-content');

/*
 * 
 * Se carga el mapa cuando termine de cargar la p'agina
 */
$(document).ready(function () {
    loadVariables();

});

var vectorGetPuntoOmb = new ol.source.Vector({
    format: new ol.format.GeoJSON(),
    url: function (extent) {
        return 'http://localhost:8084/geoserver/wfs?service=WFS&' +
                'version=1.1.0&request=GetFeature&typename=ombues:puntocerca&VIEWPARAMS=dist:' + dist + ';lon:' + lon3857 + ';lat:' + lat3857 + ";cat:1;" + dife +
                '&outputFormat=application/json&srsname=EPSG:3857&' +
                'bbox=' + extent.join(',') + ',EPSG:3857';
    },
    strategy: ol.loadingstrategy.bbox
});

var vectorGetPuntoRest = new ol.source.Vector({
    format: new ol.format.GeoJSON(),
    url: function (extent) {
        return 'http://localhost:8084/geoserver/wfs?service=WFS&' +
                'version=1.1.0&request=GetFeature&typename=ombues:puntocerca&VIEWPARAMS=dist:' + dist + ';lon:' + lon3857 + ';lat:' + lat3857 + ";cat:2;" + dife +
                '&outputFormat=application/json&srsname=EPSG:3857&' +
                'bbox=' + extent.join(',') + ',EPSG:3857';
    },
    strategy: ol.loadingstrategy.bbox
});

var vectorGetZonaOmb = new ol.source.Vector({
    format: new ol.format.GeoJSON(),
    url: function (extent) {
        return 'http://localhost:8084/geoserver/wfs?service=WFS&' +
                'version=1.1.0&request=GetFeature&typename=ombues:zonacerca&VIEWPARAMS=dist:' + dist + ';lon:' + lon3857 + ';lat:' + lat3857 +
                '&outputFormat=application/json&srsname=EPSG:3857&' +
                'bbox=' + extent.join(',') + ',EPSG:3857';
    },
    strategy: ol.loadingstrategy.bbox
});
//&VIEWPARAMS=lon:' + lon3857+ ';lat:' + lat3857

var vectorPuntoOmbu = new ol.layer.Vector({
    source: vectorGetPuntoOmb,
    name: 'Puntos ombu',
    style: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'rgba(0, 0, 255, 1.0)',
            width: 2
        }), image: new ol.style.Icon(({
            scale: 0.2,
            anchor: [0.5, 0.5],
            anchorXUnits: 'fraction',
            src: 'media/images/ombu.png'
        }))
    })
});

var vectorPuntoRestaurant = new ol.layer.Vector({
    source: vectorGetPuntoRest,
    style: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'rgba(0, 0, 255, 1.0)',
            width: 2
        }), image: new ol.style.Icon(({
            scale: 0.2,
            anchor: [0.5, 0.5],
            anchorXUnits: 'fraction',
            src: 'media/images/restaurant.png'
        }))
    })
});

var vectorZonaOmbu = new ol.layer.Vector({
    source: vectorGetZonaOmb,
    name: 'Zona ombu',
    style: new ol.style.Style({
        fill: new ol.style.Fill({
            color: 'rgba(0, 0, 0, 0.8)'
        }),
    })
})


function loadVariables() {
    formatWFS = new ol.format.WFS();
    formatGMLPunto = new ol.format.GML({
        featureNS: 'ombues',
        featureType: 'punto_ombu',
        srsName: SRS
    });

    formatGMLZona = new ol.format.GML({
        featureNS: 'ombues',
        featureType: 'zona_ombu',
        srsName: SRS
    })
    if (navigator.geolocation)
    {
        navigator.geolocation.getCurrentPosition(posicion, errorGPS);

    } else
    {
        loadMap(null);
    }
    urlGeoserverWFS = $("#url_wfs").text().trim();
    SRS = $("#srs").text().trim();
    urlGeoserverWMS = $("#url_wms").text().trim();
    cantCat = $("#catCant").text().trim();
    wms_punto = new ol.source.TileWMS({
        url: urlGeoserverWMS, // + '/wms',
        params: {'LAYERS': 'ombues:punto_ombu,ombues:zona_ombu'},
        serverType: 'geoserver',
        crossOrigin: 'anonymous'
    });

    // Crea una capa vectorial donde se modificaran los datos
    vector_layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            params: {srs: SRS}

        }),
        /*
         * Se define el estilo que tendra esta capa, en caso de cargar la capa por este medio
         * acá abría que definir la logica de estilos pra cada categoria
         */
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(0, 0, 0, 0.8)'
            }),
            stroke: new ol.style.Stroke({
                color: '#ffcc33',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ffcc33'
                })
            })
        })
    });



}
function posicion(posicion) {
    var point = ol.proj.fromLonLat([posicion.coords.longitude, posicion.coords.latitude]);
    lat3857 = point[1];
    lon3857 = point[0];
    loadMap(point);
}
function errorGPS(error) {

    alert('Error: ' + error.code + ' ' + error.message + '\n\nPor favor compruebe que está conectado ' +
            'a internet y habilite la opción permitir compartir ubicación física');

}

/*
 * 
 * funcion que carga el mapa, lo define, le carga el srs resolucion, y el target
 */

function loadMap(point) {
    if (point === null) {
        view = new ol.View({
            center: [-6252047.295729297, -4147996.053508715],
            zoom: 15
        });
    } else {
        dist = 50000;
        view = new ol.View({
            center: point,
            zoom: 15
        });
    }

    bounds = new ol.extent.boundingExtent(-6282053.606645496, -4160620.3236187138, -6236191.389674391, -4114758.106647608);
    map = new ol.Map({
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            }), /* new ol.layer.Tile({
             source: wms_punto
             })
             ,*/
            vector_layer, vectorPuntoOmbu, vectorZonaOmbu, vectorPuntoRestaurant
                    /*,
                     new ol.layer.Tile({ 
                     source: new ol.source.TileWMS({
                     url: 'http://localhost:8084/geoserver/wms',
                     params: {srs: 'EPSG:32721', layers : 'ombues:Montevideo', version : '1.3.0', styles: '', format:'image/png'}
                     }) 
                     })*/
        ],
        renderer: 'canvas',
        target: 'map',
        maxExtent: bounds,
        maxResolution: 0.703125,
        projection: SRS,
        units: 'm',
        view: view
    });

    // add the draw interaction when the page is first shown

    var feature;
    map.addControl(new ol.control.ZoomSlider());
    /*
     * Define la accion de al hacer click en el mapa muestra la informacion de la capa de puntos
     * vamos a tener que ver como diferenciar esto con zonas, que podria ser cargando todo junto en el layer
     */
    /*
     *Este es el codigo necesario para poder utilizar lo del icono una vez se defina como vector layer
     */
    /*
     var element = document.getElementById('popup');
     
     var popup = new ol.Overlay({
     element: element,
     positioning: 'bottom-center',
     stopEvent: false
     });
     map.addOverlay(popup);
     */
    map.on('singleclick', function (evt) {
        map.getInteractions().forEach(function (interaction) {

            if (interaction instanceof ol.interaction.Draw) {
                console.log(interaction + "Draw");
                var viewResolution = view.getResolution();
                var wms_numPuerta = new ol.source.TileWMS({
                    url: urlGeoserverWMS, // + '/wms',
                    params: {'LAYERS': 'ombues:getNumPuerta',
                        'VIEWPARAMS': 'lon:' + evt.coordinate[0] + ';lat:' + evt.coordinate[1]},
                    serverType: 'geoserver',
                    crossOrigin: 'anonymous'
                });
                var url2 = wms_numPuerta.getGetFeatureInfoUrl(
                        evt.coordinate, viewResolution, SRS,
                        {'INFO_FORMAT': 'application/json',
                            'propertyName': 'nom_calle,num_puerta'});
                if (url2) {
                    var parser = new ol.format.GeoJSON();
                    $.ajax({
                        url: url2,
                        dataType: 'json',
                        jsonpCallback: 'parseResponse'
                    }).then(function (response) {
                        var result = parser.readFeatures(response);
                        if (result.length) {
                            var calle = result[0].get('nom_calle');
                            var puerta = result[0].get('num_puerta');
                            document.getElementById("direccion").value = calle + " " + puerta;
                        }
                    })
                }
            } else {
                console.log(interaction + "Select");
                var feature = map.forEachFeatureAtPixel(evt.pixel,
                        function (feature) {
                            return feature;
                        });
                if (feature) {
                    getOmbu(feature.get('ombu_id'));


                }
            }
        }
        )
    }
    )

    /*  var url = wms_punto.getGetFeatureInfoUrl(
     evt.coordinate, viewResolution, SRS,
     {'INFO_FORMAT': 'application/json',
     'propertyName': 'ombu_id'});
     if (url) {
     var parser = new ol.format.GeoJSON();
     $.ajax({
     url: url,
     dataType: 'json',
     jsonpCallback: 'parseResponse'
     }).then(function (response) {
     var result = parser.readFeatures(response);
     if (result.length) {
     var info = result[0].get('ombu_id');
     getOmbu(info);
     }
     })
     
     }*/


}
;



/*
 * Empiezan las funciones de dibujar, modificar y eliminar features,
 * 
 */

// rebuild interaction when changed
$('[name="interaction_type"]').on('click', function (e) {
    // add new interaction
    if (this.value === 'draw') {
        addDrawInteraction();
    } else {
        addModifyInteraction();
    }
});


// rebuild interaction when the geometry type is changed
function regzona() {
    map.removeInteraction(draw_interaction);
    map.removeInteraction(select_interaction);
    console.log("Tengo solo add polygon");
    addDrawInteraction('Polygon');
}
;
// rebuild interaction when the geometry type is changed
function regpunto() {
    map.removeInteraction(draw_interaction);
    map.removeInteraction(select_interaction);
    addDrawInteraction('Point');
}
;
function regRef() {
    map.removeInteraction(draw_interaction);
    addSelectInteraction();
}
;
function busca() {
    console.log("Tengo solo select");
    map.removeInteraction(draw_interaction);
    addSelectInteraction();
}
;

function addSelectInteraction() {
    select_interaction = new ol.interaction.Select({
        // make sure only the desired layer can be selected

    });
    map.addInteraction(select_interaction);
}


// creates a draw interaction

function addDrawInteraction(geom) {
    // remove other interactions

    map.removeInteraction(select_interaction);
    map.removeInteraction(modify_interaction);

    // create the interaction
    draw_interaction = new ol.interaction.Draw({
        source: vector_layer.getSource(),
        type: /** @type {ol.geom.GeometryType} */ (geom)
    });

    // add it to the map
    map.addInteraction(draw_interaction);

    // when a new feature has been drawn...
    draw_interaction.on('drawend', function (event) {

        /*
         * Se agrega el clearMap para que solo quede un punto extra agregado en el mapa
         */
        clearMap();
        /*
         * Setea la geometria del feature y lo carga en una variable global para usar al confirmar ingresar
         */
        event.feature.set("geom", event.feature.getGeometry());
        feature = event.feature;
        lon3857 = event.feature.getGeometry().getCoordinates()[0];
        lat3857 = event.feature.getGeometry().getCoordinates()[1];
        lonlat = ol.proj.transform(event.feature.getGeometry().getCoordinates(), 'EPSG:3857', 'EPSG:4326');
        lon = lonlat[0];
        lat = lonlat[1];
        $('#ubicacion').val(lon + "; " + lat);
    });
}



// clear map when user clicks on 'Delete all features'
$("#delete").click(function () {
    clearMap();
});

// clears the map and the output of the data
function clearMap() {
    vector_layer.getSource().clear();
    if (select_interaction) {
        select_interaction.getFeatures().clear();
    }
    $('#data').val('');
}

/*
 * Registra el ombu mandando una llamada ayax al servidor ingresando la base del mismo
 * una vez hay respuesta manda un wfs al servidor con los datos necesarios e ingresa el punto
 * recarga la pagina al terminar
 */
function registrarOmbu() {
    var nombre = $("#nombre").val();
    var descripcion = $("#descripcion").val();
    var direccion = $("#direccion").val();
    var ubicacion = $("#ubicacion").val();
    var quees = $("#categoria").val();
    $.post("Puntos/InsertPuntoOmbu", {
        nombre: nombre, descripcion: descripcion, direccion: direccion, ubicacion: ubicacion, quees: quees
    }, function (responseText) {
        feature.set("ombu_id", responseText);
        feature.set("id_categoria", quees);
        transactWFS('insert', feature, formatGMLPunto);
        alert("Realizado correctamente");
        location.reload();
    });
}
function registrarRefOmbu() {
    var nombre = $("#refnombre").val();
    var descripcion = $("#refdescripcion").val();
    var referencia = $("#refRef").val();
    var quees = $("#refcategoria").val();
    $.post("Puntos/InsertRefOmbu", {
        nombre: nombre, descripcion: descripcion, referencia: referencia, quees: quees
    }, function (responseText) {
        alert("Realizado correctamente");
        location.reload();
    });
}

/*
 * Funcion que registrauna zona de ombues y dsps hace el transaction insert
 */
function registrarZonaOmbu() {
    var nombre = $("#zonanombre").val();
    var descripcion = $("#zonadescripcion").val();
    var direccion = $("#zonadireccion").val();
    var ubicacion = $("#zonaubicacion").val();
    var quees = 'zona';
    $.post("Puntos/InsertPuntoOmbu", {
        nombre: nombre, descripcion: descripcion, direccion: direccion, ubicacion: ubicacion, quees: quees
    }, function (responseText) {
        feature.set("ombu_id", responseText);
        transactWFS('insert', feature, formatGMLZona);
        alert("Realizado correctamente");
        location.reload();
    });
}

/*
 * Funcion que devuelve el html con los datos del ombu padre
 */
function getOmbu(ombu_id) {
    var id = ombu_id;
    ombuId = id;
    $.post("getombu", {
        id: id
    }, function (responseText) {

        document.getElementById('info-body').innerHTML =
                responseText;
        $("#modalInfo").modal('show');
    });
}

function getReport(repo_id) {
    var id = repo_id;
    $.post("getreport", {
        id: id
    }, function (responseText) {

        document.getElementById('repo-body').innerHTML =
                responseText;
        $("#modalReport").modal('show');
    });
}

function passpercentage(json) {
 var title="";
 var kind=document.getElementById("select-report").value;
    if (kind==="1"){
        title='Categoria'
    }else if (kind==="2"){
        title='Barrios';
    }
 
    $(function () {
 
        var len = json.passpercentage.length
        i = 0;
 
        var options = {
             chart: { 
				type: 'column',
				options3d: {
					enabled: true,
					alpha: 10,
					beta: 25,
					depth: 70
				}
 
                },
             credits: {
                 enabled: false
                },
                title: {
                    text: 'Ranking de ombues por '+title
                 },
//                subtitle: {
//                    text: 'Source: Test Data',
//                    x: -20
//                },
              yAxis: {
                min: 0,
                title: {
                    text: 'Cantidad'
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            xAxis: {
                categories: []
            },
            series: []
        }
 
            for (i; i < len; i++) {
                if (i === 0) {
                    var dat = json.passpercentage[i].category,
                        lenJ = dat.length,
                        j = 0,
                        tmp;
 
                    for (j; j < lenJ; j++) {
                        options.xAxis.categories.push(dat[j]);
                    }
                } else {
                    options.series.push(json.passpercentage[i]);
                }
            }
 
        $('#container').highcharts(options);
 
    });
 
    }
 
function generateChart()
{
var kind=document.getElementById("select-report").value;
chartType="passpercentage";
$("#container").text("");
 
     $.ajax({
            type: "GET",
            url:"http://localhost:8084/TSIG/getreport?jsonp="+chartType+"&kindreport="+kind,
            dataType: 'jsonp',
            jsonpCallback: chartType, // the function to call
            error: function () {
                   alert("Ha ocurrido un error");
                    }
            }); 
  $("#modalReport").modal('show');
}

function realizarComentario(ombu_id) {
    var id = ombu_id;
    var comentario = document.getElementById("comentario").value;
    $.post("ingresarComentario", {
        id: id, comentario: comentario
    }, function (responseText) {
        alert("Ingresado");
    });
}



function transactWFS(p, f, feature) {
    switch (p) {
        case 'insert':
            node = formatWFS.writeTransaction([f], null, null, feature);
            removeLowerCaseGeometryNodeForInsert(node);
            break;
        case 'update':
            node = formatWFS.writeTransaction(null, [f], null, feature);
            break;
        case 'delete':
            node = formatWFS.writeTransaction(null, null, [f], feature);
            break;
    }
    s = new XMLSerializer();
    str = s.serializeToString(node);
    $.ajax(urlGeoserverWFS, {// + '/wfs', {
        type: 'POST',
        dataType: 'xml',
        processData: false,
        contentType: 'text/xml',
        data: str
    }).done();
}
function removeLowerCaseGeometryNodeForInsert(node)
{

    var geometryNodes = node.getElementsByTagName("geometry"), element;
    while (geometryNode = geometryNodes[0])
    {
        geometryNode.parentNode.removeChild(geometryNode);
    }

}

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {

    }
}
function showPosition(position) {
    console.log(ol.proj.fromLonLat([position.coords.longitude, position.coords.latitude]));

}




function buscarCerca() {
    dist = 0;
    vectorPuntoOmbu.setSource();
    vectorZonaOmbu.setSource();
    vectorPuntoRestaurant.setSource();

    var nombre = document.getElementById("nombrebusca").value;
    var categoria = document.getElementById("categoriaBusca").value;
    var index = $("#categoriaBusca option:selected").index();
    if (nombre.length != 0) {
        dife = "comp:"+nombre;
    } else {
        dife = "";
    }
    if (categoria == 0) {
        dist = document.getElementById("distancia").value;
        vectorPuntoRestaurant.setSource(vectorGetPuntoRest);
        vectorPuntoOmbu.setSource(vectorGetPuntoOmb);
        vectorZonaOmbu.setSource(vectorGetZonaOmb);
        $.post("getNoReferencial", {
            cat: categoria, nombre: nombre
        }, function (responseText) {

            document.getElementById('myResult').innerHTML =
                    responseText;

        });
    } else {
        if (index > cantCat) {
            $.post("getNoReferencial", {
                cat: categoria, nombre: nombre
            }, function (responseText) {

                document.getElementById('myResult').innerHTML =
                        responseText;

            });
        } else {
            dist = document.getElementById("distancia").value;
            if (categoria == 1) {
                vectorPuntoOmbu.setSource(vectorGetPuntoOmb);
            }
            if (categoria == 2) {
                vectorPuntoRestaurant.setSource(vectorGetPuntoRest);

            }
            if (categoria == 99) {
                vectorZonaOmbu.setSource(vectorGetZonaOmb);

            }
            document.getElementById('myResult').innerHTML = "";
        }

    }
    vectorGetPuntoOmb.clear();
    vectorGetZonaOmb.clear();
    vectorGetPuntoRest.clear();

}
function loadDropZoneListeners(){
    dropZone.on("sending", function(file, xhr, formData){
        
        formData.append("ombuId", ombuId);
        console.log("FormatData ombuID: " + formData.get("ombuId"));
    });
}