/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl.realtime;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import dao.dslam.factory.DslamDAOFactory;
import java.util.Locale;
import model.fulltest.operacional.CustomerMock;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author G0042204
 */
public class ValidadorVlanMulticastIT {

    public ValidadorVlanMulticastIT() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of consultar method, of class ValidadorVlanMulticast.
     */
    @Test
    public void testConsultar() throws Exception {
        try {
            System.out.println("consultar");
            EfikaCustomer cust = CustomerMock.getCustomer("4130497359");
            cust.getServicos().setTipoTv(null);
            ValidadorVlanMulticast instance = new ValidadorVlanMulticast(DslamDAOFactory.getInstance(cust.getRede()), cust, new Locale("manobra", "CO"));;
            ValidacaoResult result = instance.validar();


            assertTrue(result.getResultado());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
