/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dslam.consulta.gpon;

import br.net.gvt.efika.customer.EfikaCustomer;
import model.fulltest.validacao.Validador;

/**
 *
 * @author G0041775
 */
public class SerialOntGpon implements Validador{
    private String serial;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public Boolean validar(EfikaCustomer e) {
        return !this.serial.isEmpty();
    }
    
}
