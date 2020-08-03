/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibebe.dao;

import com.mibebe.bean.Pediatra;

/**
 *
 * @author Cuenca
 */
public class Main{ 
        
    public static void main(String... arg) {
        PediatraDao dao = new PediatraDao();
        Pediatra pediatra = new Pediatra();
        pediatra.setApellidos("Cuenca Araujo");
        pediatra.setCedula("CEDULA");
        pediatra.setNombre("Yadsiry");
        pediatra.setCedulaEspecialidad("CEDULA-ESPECIALIDAD");
        pediatra.setCmcp("CMCP");
        pediatra.setSalt("SALT");
        pediatra.setCorreo("yad@gmal.com");
        pediatra.setPassword("123");
        
        dao.obtenerLista();
    }   
}