$(document).ready(function () {
    var now = new Date();
    $("#fecRegistro").val(now.toLocaleString());
    $("#fecRegistrou").val(now.toLocaleString());
    getpediatras();
    getrecetas();
    $(".recargar").click(function () {
        location.reload();
    });
    $('#re-receta').on('submit', function (e) {
        e.preventDefault();
        let form = $(this);
        const tbody = $('#dt_medicamento')[0];
        let data = {
            nombre: e.target.elements.nombre.value,
            detalles: []
        };
        for (let i = 0; i < tbody.children.length; i++) {

            data.detalles.push({
                medicamento: tbody.children[i].children[0].innerHTML,
                dosis: tbody.children[i].children[1].innerHTML,
                duracion: tbody.children[i].children[2].innerHTML,
                intervalo: tbody.children[i].children[3].innerHTML
            });
        }

        $.ajax({
            url: 'api/administrador/receta',
            type: 'PUT',
            dataType: 'json',
            data: JSON.stringify(data),
            cache: false,
            contentType: 'application/json'
        }).done(function (response) {
            if (response.data) {
                // window.location.replace(context + "/administrador");
                alert("Receta registrada correctamente");
                location.reload();
            } else {

                alert(response.message);

            }
        });
    });
    $('#frm_medicamento').on('submit', function (e) {
        e.preventDefault();
        let el = e.target.elements;
        $('#dt_medicamento').append(`
            <tr>
                <td>${el.medicamento.value}</td>
                 <td>${el.dosis.value}</td>
                 <td>${el.duracion.value}</td>
                 <td>${el.intervalo.value}</td>
            </tr>
        `);
        e.target.reset();
        $('#close_modal_medicamentos').click();
    });


    $('#up-receta').on('submit', function (e) {
        e.preventDefault();
        let form = $(this);
        const tbody = $('#dt_medicamentou')[0];
        let data = {
            nombre: e.target.elements.nombre.value,
            id: e.target.elements.id.value,
            detalles: []
        };

        for (let i = 0; i < tbody.children.length; i++) {

            data.detalles.push({
                medicamento: tbody.children[i].children[0].children[0].value,
                dosis: tbody.children[i].children[1].children[0].value,
                duracion: tbody.children[i].children[2].children[0].value,
                intervalo: tbody.children[i].children[3].children[0].value
            });
        }


        $.ajax({
            url: 'api/administrador/updateR/',
            type: 'PUT',
            dataType: 'json',
            data: JSON.stringify(data),
            cache: false,
            contentType: 'application/json'
        }).done(function (response) {
            if (response.data) {
                // window.location.replace(context + "/administrador");
                alert("Receta actualizada correctamente");
                location.reload();
            } else {

                alert(response.message);

            }
        });
    });

    $("#recetas").click(function () {
        $("#recetasS").show(350);
        $("#altaS").hide(350);
    });

    $("#altaD").click(function () {
        $("#altaS").show(350);
        $("#recetasS").hide(350);


    });
});
$('#recetas-cre').delegate('.actualizar', 'click', function (e) {
    e.preventDefault();
    let id = e.target.id;
    $("#idu").val(id);
    $("#myModalUpdate").modal();
    $("#myModal").modal('hide');

    getDatosById(id);
    getDetallesById(id);

});

$('.logout').on('click', function (e) {
    e.preventDefault();
    $.ajax({
        url: 'api/administrador/logout/',
        type: 'DELETE',
        dataType: 'json',
        data: "",
        cache: false,
        contentType: 'application/json'
    }).done(function (response) {
        if (response.OK) {

            window.location.reload();
        } else {

        }
    });
});

$('#recetas-cre').delegate('.eliminar', 'click', function (e) {
    e.preventDefault();
    let id = e.target.id;

    $.ajax({
        url: `api/administrador/deleteRecetas/${id}`,
        type: 'DELETE',
        dataType: 'json',
        data: "",
        cache: false,
        contentType: 'application/json'
    }).done(function (response) {

        if (response.data) {
            $('#recetas-cre').empty();
            alert("se ha eliminado una receta correctamente");
            getrecetas();
        } else {
            alert("Ocurrio un error");
        }
    });
});
function getrecetas() {
    $.ajax({
        url: 'api/administrador/getrecetas/',
        type: 'POST',
        dataType: 'json',
        data: "",
        cache: false,
        contentType: 'application/json'
    }).done(function (response) {
//        console.log(response.data);
        if (response.data != null) {
            for (var i = 0; i < response.data.length; i++) {
//                if (response.data[i].id==response.data[i].detalles[i].receta_id) {
//                    console.log( "Enfermedad " + response.data[i].nombre +   " Medicamento " + response.data[i].detalles[i].medicamento);
//                }
//                

                $('#recetas-cre').append(`<tr>
                                               <td style="min-width: 200px;">
                                                                    <a class="avatar" >Recetas</a>
                                                                    <h2><a >${response.data[i].nombre}<span>${response.data[i].fecRegistro}</span></a></h2>
                                                                </td>                 
                                                                <td>
                                                                    <h4 class="time-title p-0">Detalles</h4>
                                                                    <p><strong>Medicamento</strong>: ${response.data[i].detalles[i].medicamento}</p>
                                                                    <p><strong>Dosis</strong>: ${response.data[i].detalles[i].dosis}</p>
                                                                </td>
                                                                <td>
                                                                     <p><strong>Duracion</strong>: ${response.data[i].detalles[i].duracion}</p>
                                                                    <p><strong>Intervalo</strong>: ${response.data[i].detalles[i].intervalo}</p>
                                                                </td>
                                                                <td class="text-right">
                                                                    <a  class="btn btn-outline-primary take-btn actualizar" id="${response.data[i].id}">Actualizar</a><br><br>
                                                               <a  class="btn btn-outline-danger take-btn eliminar" id="${response.data[i].id}">eliminar</a>
                                                                </td>
                                                            </tr>`);
            }
        }

    });
}

function  getpediatras() {
    $.ajax({
        url: 'api/administrador/getPediatras/',
        type: 'POST',
        dataType: 'json',
        data: "",
        cache: false,
        contentType: 'application/json'
    }).done(function (response) {

        //sendEmail(25, 'yadsirycuenca@gmail.com');

        console.log(response);
        if (response.data != null) {


            for (var i = 0; i < response.data.length; i++) {
                var status = response.data[i].aprobado;
                var sta = "Alta";
                let IDe = response.data[i].id;
                var email = `${response.data[i].correo}`;


                var apro = '<span class="custom-badge status-red">Aprobar</span>';

                if (status) {
                    sta = "Baja";
                    apro = '<span class="custom-badge status-green">Aprobado</span>';
                }

                $('.pediatras').append(`<div class="col-md-4 col-sm-4  col-lg-3">
                        <div class="profile-widget">
                            <div class="doctor-img">
                                <a class="avatar" ><img alt="" src="images/login.png"></a>
                            </div>
                           <h4 class="doctor-name text-ellipsis">${response.data[i].nombre + "  " + response.data[i].apellidos}</h4>
                            <div class="doc-prof">Cedula Profesional: ${response.data[i].cedula}</div>
                            <div class="doc-prof">Cedula Especialización: ${response.data[i].cedulaEspecialidad}</div>
                            <div class="doc-prof">Cmcp: ${response.data[i].cmcp}</div>
                            <div class="doc-prof">Lugar de estudios: ${response.data[i].lugarEstudios}</div>
                            <div class="doc-prof">Fecha de Registro: ${response.data[i].fecRegistro}</div>
            
                            <div class="doc-prof"> <button style="background: transparent; border: none;" type="submit" id="${IDe}" onclick="sendEmail(this.id, '${email}')" >${apro}</button></div>
                            <div class="user-country">
                                <i class="fa fa-map-marker"></i>${response.data[i].lugarAtencion.direccion}
                            </div>
                        </div>
                    </div>`);
            }
            $("#load").hide(350);
        } else {
            alert("No hay Pediatras que mostrar");
        }

    });

}
function getDatosById(id) {
    $.ajax({
        url: `api/administrador/getDatosById/${id}`,
        type: 'POST',
        dataType: 'json',
        cache: false
    }).done(function (data) {
        $("#idu").val(data.data[0].id);
        $("#nombreu").val(data.data[0].nombre);
    });
}



//$("#t-ejemplo tr th").bind("click", headerClick);
$("#t-ejemplo tr th").on('click', function () {
    $(this).css('color', 'red');
});

$("#t-ejemplo tr td").bind("click", dataClick);
$("#saveButton").bind("click", saveButton);

function headerClick(e) {
    console.log(e);
    $(e.currentTarget).css({color: "red"});
}

function dataClick(e) {
    console.log(e);
    //if (e.currentTarget.innerHTML != "") return;
    if (e.currentTarget.contentEditable != null) {
        $(e.currentTarget).attr("contentEditable", true);
    } else {
        debugger;
        alert("que paso");
        // $(e.currentTarget).append("<input type='text' maxlength='4' size='4'/>");
    }
}
function saveButton() {
    $("table.table tr td").each(function (td, index) {
        console.log(td);
        console.log(index);
    });
}


//$('#t-ejemplo').dataTable({ });



function getDetallesById(id) {
    $.ajax({
        url: `api/administrador/getDetallesById/${id}`,
        type: 'POST',
        dataType: 'json',
        cache: false
    }).done(function (data) {

//        $("#medicamentou").val(data.data[0].medicamento);
//        $("#dosisu").val(data.data[0].dosis);
//        $("#duracionu").val(data.data[0].duracion);
//        $("#intervalou").val(data.data[0].intervalo);
        $('#dt_medicamentou').empty();
        for (var i = 0; i < data.data.length; i++) {


            $('#dt_medicamentou').append(`
            <tr>
                <td><input type="text" value="${data.data[i].medicamento}" ></td>
                 <td><input type="text" value="${data.data[i].dosis}" ></td>
                 <td><input type="text" value="${data.data[i].duracion}" ></td>
                 <td><input type="text" value="${data.data[i].intervalo}" ></td>
            </tr>
        `);
        }
    });
}

function sendEmail(val, mail) {
    $.ajax({
        url: `api/administrador/updatePediatra/${val}/${mail}`,
        type: 'POST',
        dataType: 'json',
        cache: false
    }).done(function (data) {

        if (data.data) {
            alert("Se aprobo el pediatra correctamente");
            location.reaload();
        }
    }
    );
}

// Other function
function multiplicarInputs(text) {
    var num = text.value
    var div = '';
    for (var i = 0; i < num; i++) {
        var cont = i + 1;
        div += "<br> Input text " + cont + "<input maxlength='5' name='inputTextMulti[]' size='6' type='text' />&nbsp;";
    }

    document.getElementById("divMultiInputs").innerHTML = div;
}

JSON.from = function toJson(form) { //Transformar form a json
    let data = {};
    form = form[0].elements;
    for (let i = 0; i < form.length; i++) {
        if (form[i].type != 'button' && form[i].type != 'submit') {
            let aux = {};
            if (form[i].dataset.attribute) //si el elemento tiene este atributo, significa que es un valor padre
                aux[form[i].dataset.attribute] = $(form[i]).val();
            else //No tiene valor padre y solo se pasa el valor directo al json
                aux = $(form[i]).val();
            data[form[i].name] = aux;
        }
    }
    return data;
}
    