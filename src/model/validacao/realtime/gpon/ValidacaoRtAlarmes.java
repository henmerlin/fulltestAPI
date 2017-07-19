/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.realtime.gpon;

import br.net.gvt.efika.customer.EfikaCustomer;
import dao.dslam.impl.AbstractDslam;
import model.validacao.ValidacaoAlarme;
import model.validacao.realtime.ValidacaoRealtimeGpon;

/**
 *
 * @author G0042204
 */
public class ValidacaoRtAlarmes extends ValidacaoRealtimeGpon {

    private ValidacaoAlarme valid;

    public ValidacaoRtAlarmes(AbstractDslam dslam, EfikaCustomer cust) {
        super(dslam, cust, "Lista de Alarmes");
    }

    @Override
    public Boolean validar() {
        try {
            valid = new ValidacaoAlarme(cg.getAlarmes(cust.getRede()), cust);
            valid.validar();
            this.merge(valid);
            return valid.getResultado();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
