/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import model.log.AcaoEnum;
import util.GsonUtil;

/**
 *
 * @author G0034481
 */
@Entity
@Table(name = "FULLTEST_LOGS")
public class LogEntity extends AbstractEntity {

    @Lob
    @Column(columnDefinition = "LONGVARCHAR")
    private String entrada;

    @Lob
    @Column(columnDefinition = "LONGVARCHAR")
    private String saida;

    @NotNull(message = "Campo obrigatório")
    @Size(min = 1)
    private String executor;

    @NotNull(message = "Campo obrigatório")
    @Enumerated(EnumType.STRING)
    private AcaoEnum acao;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataLog;

    public LogEntity() {
        dataLog = Calendar.getInstance();
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(Object entrada) {
        try {
            this.entrada = GsonUtil.serialize(saida);
        } catch (Exception e) {
            this.entrada = "Falha ao serializar.";
        }
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(Object saida) {
        try {
            this.saida = GsonUtil.serialize(saida);
        } catch (Exception e) {
            this.saida = "Falha ao serializar.";
        }
    }

    public Calendar getDataLog() {
        return dataLog;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public AcaoEnum getAcao() {
        return acao;
    }

    public void setAcao(AcaoEnum acao) {
        this.acao = acao;
    }

}
