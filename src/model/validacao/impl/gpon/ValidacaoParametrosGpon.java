/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl.gpon;

import br.net.gvt.efika.customer.EfikaCustomer;
import model.dslam.consulta.gpon.TabelaParametrosGpon;
import model.validacao.impl.ValidacaoValidavel;

/**
 *
 * @author G0041775
 */
public class ValidacaoParametrosGpon extends ValidacaoValidavel {

    private final transient TabelaParametrosGpon t;

    public ValidacaoParametrosGpon(TabelaParametrosGpon t, EfikaCustomer cust) {
        super(cust, t);
        this.t = t;
    }

    @Override
    protected String frasePositiva() {
        return "Parâmetros dentro do padrão (entre -8 e -30).";
    }

    @Override
    protected String fraseNegativa() {
        return "Parâmetros fora do padrão (entre -8 e -30). Pot. OLT: " + t.getPotOlt() + ". "
                + "Pot. ONT: " + t.getPotOnt() + ". Seguir o fluxo com o problema/sintoma informado pelo cliente.";
    }

}