$(document).ready(function () {
    
    //initFunctions();

    $('.login').on('submit', function (e) {
      
        e.preventDefault();
        let form = $(this);
        let button = form[0].elements.login;
   
       
            let data = JSON.from(form);
            $.ajax({
                url: 'api/administrador/login/',
                type: 'PUT',
                dataType: 'json',
                data: JSON.stringify(data),
                cache: false,
                contentType: 'application/json'
            }).done(function (response) {
                if (response.OK) {
                  // window.location.replace(context + "/administrador");
                  location.reload();
                } else {
                    
                    alert(response.message);
                    
                }  
            });
             
    });
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
    };
});