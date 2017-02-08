/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import model.dslam.AbstractDslam;
import model.fulltest.validacao.ValidacaoFacade;
import dao.cadastro.CadastroDAO;

/**
 *
 * @author G0042204
 */
public class testValidacaoFacadeClass {

    /**
     * Alcatel: 7530301249 | Zhone: 7130520294 - 1630143618 - 8531030639(hib) |
     * Keymile: 7930272843 - 3125714804
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        try {

            CadastroDAO dao = new CadastroDAO();
            AbstractDslam ds = dao.getDslam("7530301249");

            ValidacaoFacade v = new ValidacaoFacade((AbstractDslam) ds);

            v.validar();

        } catch (Exception e) {
            System.out.println("Exception:" + e.getCause());
        }

    }

}