/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var bounds,
        map,
        vector_layer,
        // make interactions global so they can later be removed
        select_interaction,
        draw_interaction,
        modify_interaction;

$(document).ready(function () {
    loadMap();
    console.log("Listo!!");
    convert();

});





function loadMap() {
    // create a vector layer used for editing
    vector_layer = new ol.layer.Vector({
        
        name: 'my_vectorlayer',
        source: new ol.source.Vector({
            format: new ol.format.GeoJSON(),
            url: function(extent) {
          return 'http://localhost:8084/geoserver/wfs?service=WFS&' +
              'version=1.1.0&request=GetFeature&typename=ombues:punto_ombu&' +
              'outputFormat=application/json&srsname=EPSG:3857&' +
              'bbox=' + extent.join(',') + ',EPSG:3857';
        },
        strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({
          maxZoom: 19
        }))
        }),
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
    bounds = new ol.extent.boundingExtent(-6282053.606645496, -4160620.3236187138, -6236191.389674391, -4114758.106647608);
    map = new ol.Map({
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
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
        projection: "EPSG:3857",
        units: 'm',
        view: new ol.View({
            center: [-6252047.295729297, -4147996.053508715],
            zoom: 15
        })
    });

    // add the draw interaction when the page is first shown
    addDrawInteraction();
    var feature;
    map.addControl(new ol.control.ZoomSlider());

}

function actualInfo() {
    alert("F12 y mira el console log papá");
    var extent = map.getView().calculateExtent(map.getSize());
    console.log("extent actual: " + extent);
    var center = map.getView().getCenter();
    console.log("center actual: " + center);
}

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
$('#geom_type').on('change', function (e) {
    map.removeInteraction(draw_interaction);
    addDrawInteraction();
});


// clear map and rebuild interaction when changed
$('#data_type').onchange = function () {
    clearMap();
    map.removeInteraction(draw_interaction);
    addDrawInteraction();
};

// build up modify interaction
// needs a select and a modify interaction working together
function addModifyInteraction() {
    // remove draw interaction
    map.removeInteraction(draw_interaction);
    // create select interaction
    select_interaction = new ol.interaction.Select({
        // make sure only the desired layer can be selected
        layers: function (vector_layer) {
            return vector_layer.get('name') === 'my_vectorlayer';
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

function addDrawInteraction() {
    // remove other interactions
    map.removeInteraction(select_interaction);
    map.removeInteraction(modify_interaction);

    // create the interaction
    draw_interaction = new ol.interaction.Draw({
        source: vector_layer.getSource(),
        type: /** @type {ol.geom.GeometryType} */ ($('#geom_type').val())
    });
    // add it to the map
    map.addInteraction(draw_interaction);

    // when a new feature has been drawn...
    draw_interaction.on('drawend', function (event) {
        // create a unique id
        // it is later needed to delete features
        var id = uid();
        // give the feature this id
        //      event.feature.setId(20);
        // save the changed datatry
        event.feature.set("geom", event.feature.getGeometry());
        feature=event.feature;

        console.log(event.feature.getGeometry().getCoordinates());
        saveData();
    });
}

// shows data in textarea
// replace this function by what you need
function saveData() {
    // get the format the user has chosen
    var data_type = $('#data_type').val(),
            // define a format the data shall be converted to
            format = new ol.format[data_type](),
            // this will be the data in the chosen format
            data;

    try {
        // convert the data of the vector_layer into the chosen format
        data = format.writeFeatures(vector_layer.getSource().getFeatures());

    } catch (e) {
        // at time of creation there is an error in the GPX format (18.7.2014)
        $('#data').val(e.name + ": " + e.message);
        return;
    }
    if ($('#data_type').val() === 'GeoJSON') {
        // format is JSON
        $('#data').val(JSON.stringify(data, null, 4));

    } else {
        // format is XML (GPX or KML)
        var serializer = new XMLSerializer();
        $('#data').val(serializer.serializeToString(data));
    }
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

// creates unique id's
function uid() {
    var id = 0;
    return function () {
        if (arguments[0] === 0) {
            id = 0;
        }
        return id++;
    }
}

function convert() {
    console.log('hey')
    //initialize json-textarea-input
    var input = map.getView().getCenter();
    //get the input as json-object
    console.log(input);

    $('#ubicacion').val(input);
}

function registrarOmbu() {
    var nombre = $("#nombre").val();
    var descripcion = $("#descripcion").val();
    var direccion = $("#direccion").val();
    var ubicacion = $("#ubicacion").val();
    var quees = $("#categoria").val();
    $.post("Puntos/InsertPuntoOmbu", {
        nombre: nombre, descripcion: descripcion, direccion: direccion, ubicacion: ubicacion, quees: quees
    }, function (responseText) {
        feature.set("ombu_id",responseText);
        transactWFS('insert', feature);
        alert("Realizado correctamente");
        location.reload();
    });
}

var formatWFS = new ol.format.WFS();
var formatGML = new ol.format.GML({
    featureNS: 'ombues',
    featureType: 'punto_ombu',
    srsName: 'EPSG:3857'
});
var transactWFS = function (p, f) {
    switch (p) {
        case 'insert':
            node = formatWFS.writeTransaction([f], null, null, formatGML);
            removeLowerCaseGeometryNodeForInsert(node);
            break;
        case 'update':
            node = formatWFS.writeTransaction(null, [f], null, formatGML);
            break;
        case 'delete':
            node = formatWFS.writeTransaction(null, null, [f], formatGML);
            break;
    }
    s = new XMLSerializer();
    str = s.serializeToString(node);
    console.log(str);
    $.ajax('http://localhost:8084/geoserver/wfs', {
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
