/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;

import java.util.List;
import model.dslam.consulta.Porta;

/**
 *
 * @author G0041775
 */
public interface ConfigGetterService {

    public List<Porta> getterEstadoPortasProximas() throws Exception;
}