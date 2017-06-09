/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import dao.dslam.impl.login.LoginDslamStrategy;

/**
 *
 * @author G0042204
 */
public class ConsultaDslam implements Conector {

    public Socket pingSocket;
    public PrintWriter out;
    public BufferedReader in;

    public AbstractDslam dslam;

    public LoginDslamStrategy styLogin;

    public ConsultaDslam(AbstractDslam dslam) {
        this.dslam = dslam;

    }

    @Override
    public void conectar() {
        this.dslam.conectar();
    }

    public List<String> getRetorno() throws IOException {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 999999; i++) {

            String line = in.readLine();
            if (line.contains("||")) {
                break;
            }

            list.add(line);
        }
        return list;
    }

    @Override
    public void close() throws IOException {
        if (out != null) {
            out.close();
            in.close();
            pingSocket.close();
        }
    }

    public ComandoDslam consulta(ComandoDslam comando) throws Exception {

        try {

            if (pingSocket == null) {
                this.conectar();
            }

            out.println(comando.getSintax());
            Thread.sleep(comando.getSleep());
            if (comando.getSintaxAux() != null) {
                out.println(comando.getSintaxAux());
                Thread.sleep(1000);
            }
            out.println("||");
            comando.setRetorno(this.getRetorno());
            return comando;

        } catch (Exception e) {
            try {
                close();
                Thread.sleep(3000);
                conectar();
                out.println(comando.getSintax());
                Thread.sleep(comando.getSleep());
                if (comando.getSintaxAux() != null) {
                    out.println(comando.getSintaxAux());
                    Thread.sleep(1000);
                }
                out.println("||");
                comando.setRetorno(this.getRetorno());
                return comando;
            } catch (Exception ex) {
                System.out.println("Nao foi nem depois de fechar e abrir conexao Dx");
                ex.printStackTrace();
            }
        }
        return null;
    }

}
