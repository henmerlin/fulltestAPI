/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dslam.consulta.gpon;

import br.net.gvt.efika.customer.EfikaCustomer;
import model.dslam.consulta.Validavel;

/**
 *
 * @author G0041775
 */
public class SerialOntGpon implements Validavel {

    private String serial, slot, porta;

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

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

    @Override
    public String getNome() {
        return "Associação Serial ONT";
    }

}
