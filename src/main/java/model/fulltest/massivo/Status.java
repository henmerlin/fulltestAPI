/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.fulltest.massivo;

/**
 *
 * @author G0042204
 */
public enum Status {

    ATIVO("Ativo"), EM_EXECUCAO("Em execução"), CONCLUIDO("Concluído"), EXCLUIDO("Excluído");

    String nome;

    private Status(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

}
