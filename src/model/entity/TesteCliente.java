/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import model.fulltest.validacao.tipo.ValidacaoGpon;

/**
 *
 * @author G0042204
 */
@Entity
@Table(name = "fulltestAPI_TesteCliente")
public class TesteCliente extends ComponenteGenerico {

    @ManyToOne
    private Lote lote;

    @OneToMany
    private List<ValidacaoGpon> valid;

    private String instancia;

    public TesteCliente() {
        this.valid = new ArrayList<>();
    }

    public TesteCliente(String instancia) {
        this.instancia = instancia;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public String getInstancia() {
        return instancia;
    }

    public void setInstancia(String instancia) {
        this.instancia = instancia;
    }

    public List<ValidacaoGpon> getValid() {
        return valid;
    }

    public void setValid(List<ValidacaoGpon> valid) {
        this.valid = valid;
    }

}
