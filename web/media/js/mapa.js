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
        ombuId,
        // make interactions global so they can later be removed
        select_interaction,
        draw_interaction,
        modify_interaction,
        urlGeoserverWFS,
        urlGeoserverWMS,
        SRS,
        wms_punto,
        vector_layer,
        content = document.getElementById('popup-content');

/*
 * 
 * Se carga el mapa cuando termine de cargar la p'agina
 */
$(document).ready(function () {
    loadVariables();

});

function loadVariables() {
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
    wms_punto = new ol.source.TileWMS({
        url: urlGeoserverWMS, // + '/wms',
        params: {'LAYERS': 'ombues:punto_ombu,ombues:zona_ombu'},
        serverType: 'geoserver',
        crossOrigin: 'anonymous'
    });

    // Crea una capa vectorial donde se modificaran los datos
    vector_layer = new ol.layer.Vector({
        name: 'Puntos ombu',
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
    loadMap(ol.proj.fromLonLat([posicion.coords.longitude, posicion.coords.latitude]));
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
            }), new ol.layer.Tile({
                source: wms_punto
            })
                    ,
            vector_layer,
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
    addDrawInteraction('Point');
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
        document.getElementById('popup').innerHTML = '';
        var viewResolution = /** @type {number} */ (view.getResolution());
        var url = wms_punto.getGetFeatureInfoUrl(
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

        }
 
        var wms_numPuerta = new ol.source.TileWMS({
            url: urlGeoserverWMS, // + '/wms',
            params: {'LAYERS': 'ombues:getNumPuerta',
                'VIEWPARAMS':'lon:'+evt.coordinate[0]+';lat:'+evt.coordinate[1]},
            serverType: 'geoserver',
            crossOrigin: 'anonymous'
        });
        var url2 = wms_numPuerta.getGetFeatureInfoUrl(
                evt.coordinate, viewResolution, SRS,
                {'INFO_FORMAT': 'application/json',
                    'propertyName': 'nom_calle,num_puerta'});
                console.log(url2);
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
                    document.getElementById("direccion").value=calle + " " +puerta;
                }
            })}





    });
}

/*
 * 
 * @returns los datos de centro y borde del mapa mostrado
 */
function actualInfo() {
    var extent = map.getView().calculateExtent(map.getSize());
    console.log("extent actual: " + extent);
    var center = map.getView().getCenter();
    console.log("center actual: " + center);
}

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
$('#regzona').on('click', function (e) {
    map.removeInteraction(draw_interaction);
    addDrawInteraction('Polygon');
});
// rebuild interaction when the geometry type is changed
$('#regpunto').on('click', function (e) {
    map.removeInteraction(draw_interaction);
    addDrawInteraction('Point');
});



// build up modify interaction
// needs a select and a modify interaction working together
function addModifyInteraction() {
    // remove draw interaction
    map.removeInteraction(draw_interaction);
    // create select interaction
    select_interaction = new ol.interaction.Select({
        // make sure only the desired layer can be selected
        layers: function (vector_layer) {
            return vector_layer.get('name') === 'Puntos ombu';
        }
    });
    map.addInteraction(select_interaction);

    // grab the features from the select interaction to use in the modify interaction
    var selected_features = select_interaction.getFeatures();
    // when a feature is selected...
    selected_features.on('add', function (event) {
        // grab the feature
        var feature = event.element;
        // ...listen for changes and save them
        feature.on('change', saveData);
        // listen to pressing of delete key, then delete selected features
        $(document).on('keyup', function (event) {
            if (event.keyCode == 46) {
                // remove all selected features from select_interaction and my_vectorlayer
                selected_features.forEach(function (selected_feature) {
                    var selected_feature_id = selected_feature.getId();
                    // remove from select_interaction
                    selected_features.remove(selected_feature);
                    // features aus vectorlayer entfernen
                    var vectorlayer_features = vector_layer.getSource().getFeatures();
                    vectorlayer_features.forEach(function (source_feature) {
                        var source_feature_id = source_feature.getId();
                        if (source_feature_id === selected_feature_id) {
                            // remove from my_vectorlayer
                            vector_layer.getSource().removeFeature(source_feature);
                            // save the changed data
                            saveData();
                        }
                    });
                });
                // remove listener
                $(document).off('keyup');
            }
        });
    });
    // create the modify interaction
    modify_interaction = new ol.interaction.Modify({
        features: selected_features,
        // delete vertices by pressing the SHIFT key
        deleteCondition: function (event) {
            return ol.events.condition.shiftKeyOnly(event) &&
                    ol.events.condition.singleClick(event);
        }
    });
    // add it to the map
    map.addInteraction(modify_interaction);
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
        // create a unique id
        // it is later needed to delete features
        /*
         * Se agrega el clearMap para que solo quede un punto extra agregado en el mapa
         */
        clearMap();
        /*
         * Setea la geometria del feature y lo carga en una variable global para usar al confirmar ingresar
         */
        event.feature.set("geom", event.feature.getGeometry());
        feature = event.feature;
        $('#ubicacion').val(event.feature.getGeometry().getCoordinates());
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
        feature.set("id_categoria",quees);
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

function realizarComentario(ombu_id) {
    var id = ombu_id;
    var comentario = document.getElementById("comentario").value;
    $.post("ingresarComentario", {
        id: id, comentario: comentario
    }, function (responseText) {
        alert("Ingresado");
    });
}

var formatWFS = new ol.format.WFS();
var formatGMLPunto = new ol.format.GML({
    featureNS: 'ombues',
    featureType: 'punto_ombu',
    srsName: SRS
});

var formatGMLZona = new ol.format.GML({
    featureNS: 'ombues',
    featureType: 'zona_ombu',
    srsName: SRS
})
var transactWFS = function (p, f, feature) {
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
    console.log(str);
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
