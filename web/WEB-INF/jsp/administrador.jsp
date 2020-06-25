<%-- 
    Document   : administrador
    Created on : 20/07/2019, 06:11:40 PM
    Author     : demetryo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <!-- blank-page24:04-->
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
        <link rel="shortcut icon" type="image/png" href="img/logo.png">
        <title>Administrador</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <!--[if lt IE 9]>
                    <script src="assets/js/html5shiv.min.js"></script>
                    <script src="assets/js/respond.min.js"></script>
            <![endif]-->
    </head>

    <body>
        <div class="main-wrapper">
            <div class="header">
                <div class="header-left">
                    <a href="index-2.html" class="logo">
                        <img src="img/logo.png" width="35" height="35" alt=""> <span>Pediatra Innovadora</span>
                    </a>
                </div>
                <a id="toggle_btn" href="javascript:void(0);"><i class="fa fa-bars"></i></a>
                <a id="mobile_btn" class="mobile_btn float-left" href="#sidebar"><i class="fa fa-bars"></i></a>
                <ul class="nav user-menu float-right">


                    <li class="nav-item dropdown has-arrow">
                        <a href="#" class="dropdown-toggle nav-link user-link" data-toggle="dropdown">
                            <span class="user-img"><img class="rounded-circle" src="img/user.jpg" width="40" alt="Admin">
                                <span class="status online"></span></span>
                            <span>Admin</span>
                        </a>
                        <div class="dropdown-menu">


                            <a class="dropdown-item logout" >Cerrar sesion</a>
                        </div>
                    </li>
                </ul>
                <div class="dropdown mobile-user-menu float-right">
                    <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-ellipsis-v"></i></a>
                    <div class="dropdown-menu dropdown-menu-right">
                        <a class="dropdown-item" href="profile.html">My Profile</a>
                        <a class="dropdown-item" href="edit-profile.html">Edit Profile</a>
                        <a class="dropdown-item" href="settings.html">Settings</a>
                        <a class="dropdown-item" href="login.html">Logout</a>
                    </div>
                </div>
            </div>
            <div class="sidebar" id="sidebar">
                <div class="sidebar-inner slimscroll">
                    <div id="sidebar-menu" class="sidebar-menu">
                        <ul>
                            <li class="menu-title">Menú</li>
                            <li>
                                <a id="altaD"><i class="fa fa-user-md"></i> <span>Pediatras</span></a>
                            </li>
                            <li>
                                <a id="recetas"><i class="fa fa-hospital-o"></i> <span>Recetas</span></a>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>
            <div class="page-wrapper">
                <div class="content">

                    <section id="altaS">
                        <div class="content">
                            <div class="row">
                                <div class="col-sm-4 col-3">
                                    <h4 class="page-title">Pediatras</h4>
                                </div>
                                <div class="col-sm-8 col-9 text-right m-b-20">

                                </div>
                            </div>
                            <div class="row doctor-grid pediatras">



                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="see-all">
                                        <a class="see-all-btn" id="load">Cargando..........</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </section>     

                    <section id="recetasS" style="display: none;">
                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-sm-4 col-3">
                                        <h4 class="page-title">Registrar Receta</h4>
                                    </div>
                                    <div class="col-lg-8 offset-lg-2">
                                        <form id="re-receta">
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <div class="form-group">
                                                        <label>Nombre <span class="text-danger">*</span></label>
                                                        <input required class="form-control" type="text" name="nombre" id="nombre">
                                                    </div>
                                                </div>
                                                <div class="col-sm-12">
                                                    <div class="doc-prof"><a data-toggle="modal" data-target="#modal_medicamentos" class="btn btn btn-success btn-rounded float-left"><i class="fa fa-plus"></i>Agregar Detalles</a><br><br></div>
                                                
                                                </div>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                    <th>Medicamento</th>
                                                    <th>Dosis</th>
                                                    <th>Duracion</th>
                                                    <th>Intervalo</th>
                                                </thead>
                                                <tbody id="dt_medicamento">
                                                    
                                                </tbody>
                                            </table>
                                            <div class="m-t-20 text-center">
                                                <button class="btn btn-primary submit-btn">Registrar</button><br><br>
                                                <div class="doc-prof"><a  data-toggle="modal" data-target="#myModal"><span class="custom-badge status-red">Recetas Registradas</span></a></div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- Modal -->
                        <div class="modal fade" id="myModal" role="dialog">
                            <div class="modal-dialog modal-lg">

                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button id="" type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Tus Recetas</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="card">
                                            <div class="card-header">
                                                <h4 class="card-title d-inline-block">Recetas creadas</h4>
                                            </div>
                                            <div class="card-body p-0">
                                                <div class="table-responsive">
                                                    <table class="table mb-0">
                                                        <thead class="d-none">
                                                            <tr>
                                                                <th>Enfermedad</th>
                                                                <th>Tratamiento</th>
                                                                <th>Fecha</th>
                                                                <th class="text-right">Status</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody id="recetas-cre">


                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </div>
                        </div>    


                        <!-- Modal -->
                        <div class="modal fade" id="myModalUpdate" role="dialog">
                            <div class="modal-dialog modal-lg">

                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Tus Recetas</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="card">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-sm-4 col-3">
                                                        <h4 class="page-title">Actualizar Receta</h4>
                                                    </div>
                                                    <div class="col-lg-8 offset-lg-2">
                                                        <form id="up-receta">
                                                            <input type="hidden" id="idu" name="id">           
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <div class="form-group">
                                                        <label>Nombre <span class="text-danger">*</span></label>
                                                        <input required="" class="form-control" type="text" name="nombre" id="nombreu">
                                                    </div>
                                                </div>
                                                <div class="col-sm-12">
                                                    <div class="doc-prof"><a data-toggle="modal" data-target="#modal_medicamentos" class="btn btn btn-success btn-rounded float-left"><i class="fa fa-plus"></i>Agregar Detalles</a><br><br></div>
                                                
                                                </div>
                                            </div>
                                       <div class="table-responsive">
                                            <table id="t-ejemplo" class="table table-bordered">
                                                <thead class="mbhead">
                                                  <tr class="mbrow"><th>Medicamento</th>
                                                    <th>Dosis</th>
                                                    <th>Duracion</th>
                                                    <th>Intervalo</th>
                                                </tr></thead>
                                                <tbody id="dt_medicamentou">
                                                    
                                                </tbody>
                                            </table>
                                             </div>
                                            <div class="m-t-20 text-center">
                                                <button class="btn btn-primary submit-btn">Actualizar</button><br><br>
                                                <div class="doc-prof"><a data-toggle="modal" data-target="#myModal"><span class="custom-badge status-red">Recetas Registradas</span></a></div>
                                            </div>
                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </div>
                        </div>    
                        
                        <div class="modal fade" id="modal_medicamentos" role="dialog">
                            <div class="modal-dialog modal-lg">

                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button id="close_modal_medicamentos" type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="modal-body">

                                        <div class="card">
                                            <div class="card-body">
                                                <div class="row">
                                                    <div class="col-lg-12">
                                                        <form id="frm_medicamento">
                                            <div class="row">
                                                <div class="col-sm-6">
                                                    <div class="form-group">
                                                        <label>Medicamento<span class="text-danger">*</span></label>
                                                        <input required class="form-control" type="text" name="medicamento" id="medicamento">
                                                    </div>
                                                </div>

                                                <div class="col-sm-6">
                                                    <div class="form-group">
                                                        <label>Dosis <span class="text-danger">*</span></label>
                                                        <input required class="form-control" type="text" name="dosis" id="dosis">
                                                    </div>
                                                </div>
                                                <div class="col-sm-6">
                                                    <div class="form-group">
                                                        <label>Duración <span class="text-danger">*</span></label>
                                                        <input required class="form-control" type="text" name="duracion" id="duracion">
                                                    </div>
                                                </div>
                                                <div class="col-sm-6">
                                                    <div class="form-group">
                                                        <label>Intervalo<span class="text-danger">*</span></label>
                                                        <input required class="form-control" type="text" name="intervalo" id="intervalo">
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="m-t-20 text-center">
                                                <button class="btn btn-primary submit-btn">Agregar</button><br><br>
                                            </div>
                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>    


                    </section>    



                </div>
            </div>
        </div>
        <div class="sidebar-overlay" data-reff=""></div>
        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.slimscroll.js"></script>
        <script src="js/administrador.js"></script>
        <script src="https://smtpjs.com/v3/smtp.js"></script>
       
        <script src="js/app.js"></script>
    </body>


    <!-- blank-page24:04-->
</html>
