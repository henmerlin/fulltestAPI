/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.fulltest.operacional.facade;

/**
 *
 * @author G0042204
 */
public class FactoryFulltest {
    
    public static FullTestInterface manobra(){
        return new FullTestManobraFacade();
    }
    
}
