/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dslam.consulta;

import br.net.gvt.efika.customer.EfikaCustomer;
import java.math.BigInteger;

/**
 *
 * @author G0041775
 */
public class VlanVod extends VlanAbstract {

    public VlanVod(BigInteger p100, BigInteger cvlan) {
        super(p100, cvlan);
    }
    

    @Override
    public Boolean validar(EfikaCustomer e) {
//        return (this.getCvlan().equals(new BigInteger(ds.getVlanVode())) && this.getP100().equals(new BigInteger(ds.getP100())));
        return false;
    }
}
