var dropZone;
$(document).ready(function (){
    dropZone = new Dropzone(
            "div#picDropzone",
            {       
                url: "Imagenes",
                uploadMultiple: true,
                acceptedFiles: "image/*",
                autoProcessQueue: false,
                previewTemplate: document.getElementById('preview-template').innerHTML,
            }
        );
})

var dropZone2;
$(document).ready(function (){
    dropZone2 = new Dropzone(
            "div#picDropzone2",
            {       
                url: "Imagenes",
                uploadMultiple: true,
                acceptedFiles: "image/*",
                autoProcessQueue: false,
                previewTemplate: document.getElementById('preview-template').innerHTML,
            }
        );
})

/*$(document).ready(function () {
    $('#fileupload').fileupload({
        url: 'Imagenes',
        sequentailUploads: true,
        dataType: 'json',
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        },
        add: function (e, data) {
            data.context = $('<button/>').text('Upload')
                .appendTo($('#imagesForm'))
                .click(function () {
                    data.context = $('<p/>').text('Uploading...').replaceAll($(this));
                    data.submit();
                });
        },
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        },
        
    });
});*/
