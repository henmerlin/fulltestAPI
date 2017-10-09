/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;

import br.net.gvt.efika.customer.EfikaCustomer;
import br.net.gvt.efika.customer.TipoRede;
import dao.dslam.factory.exception.FuncIndisponivelDslamException;
import dao.dslam.impl.AlteracaoClienteInter;
import dao.dslam.impl.AlteracaoGponDefault;
import dao.dslam.impl.AlteracaoMetalicoDefault;
import dao.dslam.impl.ConsultaClienteInter;
import dao.dslam.impl.ConsultaGponDefault;
import dao.dslam.impl.ConsultaMetalicoDefault;
import model.dslam.config.ConfiguracaoPorta;

/**
 *
 * @author G0042204
 */
public class ConfigPortaServiceImpl extends ConfigGenericService implements ConfigPortaService<ConfiguracaoPorta> {

    public ConfigPortaServiceImpl(EfikaCustomer ec) {
        super(ec);
    }

    @Override
    public ConsultaClienteInter consulta() throws Exception {
        if (this.getEc().getRede().getTipo() == TipoRede.GPON) {
            try {
                return (ConsultaGponDefault) getDslam();
            } catch (ClassCastException e) {
                throw new FuncIndisponivelDslamException();
            }
        } else {
            try {
                return (ConsultaMetalicoDefault) getDslam();
            } catch (ClassCastException e) {
                throw new FuncIndisponivelDslamException();
            }
        }
    }

    @Override
    public AlteracaoClienteInter alteracao() throws Exception {
        if (this.getEc().getRede().getTipo() == TipoRede.GPON) {
            try {
                return (AlteracaoGponDefault) getDslam();
            } catch (ClassCastException e) {
                throw new FuncIndisponivelDslamException();
            }
        } else {
            try {
                return (AlteracaoMetalicoDefault) getDslam();
            } catch (ClassCastException e) {
                throw new FuncIndisponivelDslamException();
            }
        }
    }

    @Override
    public ConfiguracaoPorta consultar() throws Exception {
        if (this.getEc().getRede().getTipo() == TipoRede.GPON) {
            return FactoryService.createConfigOLTService(this.getEc()).consultar();
        } else {
            return FactoryService.createConfigDslamService(this.getEc()).consultar();
        }
    }

}
