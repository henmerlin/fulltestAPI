/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.metalico.keymile;

import br.net.gvt.efika.efika_customer.model.customer.InventarioRede;
import br.net.gvt.efika.fulltest.model.telecom.config.ComandoDslam;
import br.net.gvt.efika.fulltest.model.telecom.properties.DeviceMAC;
import br.net.gvt.efika.fulltest.model.telecom.properties.EnumEstadoVlan;
import br.net.gvt.efika.fulltest.model.telecom.properties.Profile;
import br.net.gvt.efika.fulltest.model.telecom.properties.ProfileMetalico;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanBanda;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanMulticast;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVod;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVoip;
import br.net.gvt.efika.fulltest.model.telecom.properties.metalico.Modulacao;
import br.net.gvt.efika.fulltest.model.telecom.properties.metalico.TabelaParametrosMetalico;
import br.net.gvt.efika.fulltest.model.telecom.properties.metalico.TabelaParametrosMetalicoVdsl;
import br.net.gvt.efika.fulltest.model.telecom.velocidade.Modulacoes;
import br.net.gvt.efika.fulltest.model.telecom.velocidade.VelocidadeVendor;
import br.net.gvt.efika.fulltest.model.telecom.velocidade.Velocidades;
import dao.dslam.impl.ConsultaDslamVivo2;
import dao.dslam.impl.retorno.TratativaRetornoUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G0042204
 */
public abstract class KeymileMetalicoSuvdDslam extends KeymileMetalicoDslam {

    public KeymileMetalicoSuvdDslam(String ipDslam) {
        super(ipDslam);
        this.setCd(new ConsultaDslamVivo2(this));
    }

    @Override
    public List<VelocidadeVendor> obterVelocidadesUpVendor() {
        if (velsUp.isEmpty()) {
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_1024, "HSI_3Mb_1Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_1024, "HSI_5Mb_1Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_1024, "HSI_10Mb_1Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_1024, "HSI_15Mb_1Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_2048, "HSI_25Mb_2Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_3072, "HSI_35Mb_3Mb_SUV"));
            velsUp.add(new VelocidadeVendor(Velocidades.VEL_5120, "HSI_50Mb_5Mb_SUV"));
        }

        return velsUp;
    }

    @Override
    public List<VelocidadeVendor> obterVelocidadesDownVendor() {
        if (velsDown.isEmpty()) {
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_3072, "HSI_3Mb_1Mb_SUV", "ADSL2PLUS_AUTO_SUV", Modulacoes.AUTO_NEGOTIATE));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_5120, "HSI_5Mb_1Mb_SUV", "ADSL2PLUS_AUTO_SUV", Modulacoes.AUTO_NEGOTIATE));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_10240, "HSI_10Mb_1Mb_SUV", "ADSL2PLUS_ONLY_SUV", Modulacoes.ADSL));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_15360, "HSI_15Mb_1Mb_SUV", "ADSL2PLUS_ONLY_SUV", Modulacoes.ADSL));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_25600, "HSI_25Mb_2Mb_SUV", "VDSL_17A_B8_12_SUV", Modulacoes.VDSL));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_35840, "HSI_35Mb_3Mb_SUV", "VDSL_17A_B8_12_SUV", Modulacoes.VDSL));
            velsDown.add(new VelocidadeVendor(Velocidades.VEL_51200, "HSI_50Mb_5Mb_SUV", "VDSL_17A_B8_12_SUV", Modulacoes.VDSL));
        }

        return velsDown;
    }

    protected ComandoDslam getComandoConsultaVlan2(String srvc) {
        return new ComandoDslam("get /services/packet/" + srvc + "/cfgm/Service");
    }

    @Override
    public TabelaParametrosMetalico getTabelaParametros(InventarioRede i) throws Exception {
        ComandoDslam cmd = this.getCd().consulta(this.getVelSinc(i));
        List<String> velSinc = cmd.getRetorno();
        ComandoDslam cmd1 = this.getCd().consulta(this.getSnrAtn(i));
        List<String> atnSnr = cmd1.getRetorno();
        ComandoDslam cmd2 = this.getCd().consulta(getAttainableRate(i));
        List<String> att = cmd2.getRetorno();

        try {
            TabelaParametrosMetalicoVdsl tab = new TabelaParametrosMetalicoVdsl();
            tab.addInteracao(cmd);
            tab.addInteracao(cmd1);
            tab.addInteracao(cmd2);

            tab.setVelSincDown(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate")));
            tab.setVelSincUp(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate", 2)));
            tab.setVelMaxDown(new Double(TratativaRetornoUtil.tratKeymile(att, "Downstream")));
            tab.setVelMaxUp(new Double(TratativaRetornoUtil.tratKeymile(att, "Upstream")));
            tab.setAtnUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation")));
            tab.setSnrUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin")));
            tab.setAtnUp1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 2)));
            tab.setSnrUp1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 2)));
            tab.setAtnUp2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 3)));
            tab.setSnrUp2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 3)));
            tab.setAtnDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 4)));
            tab.setSnrDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 4)));
            tab.setAtnDown1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 5)));
            tab.setSnrDown1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 5)));
            tab.setAtnDown2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 6)));
            tab.setSnrDown2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 6)));

            return tab;
        } catch (Exception e) {

            try {
                TabelaParametrosMetalico tab = new TabelaParametrosMetalico();
                tab.addInteracao(cmd);
                tab.addInteracao(cmd1);
                tab.addInteracao(cmd2);
                tab.setVelSincDown(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate")));
                tab.setVelSincUp(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate", 2)));
                tab.setVelMaxDown(new Double(TratativaRetornoUtil.tratKeymile(att, "Downstream")));
                tab.setVelMaxUp(new Double(TratativaRetornoUtil.tratKeymile(att, "Upstream")));
                tab.setAtnUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation")));
                tab.setSnrUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin")));
                tab.setAtnDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 2)));
                tab.setSnrDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 2)));
                return tab;
            } catch (Exception ex) {
                TabelaParametrosMetalico tab = new TabelaParametrosMetalico();
                tab.addInteracao(cmd);
                tab.addInteracao(cmd1);
                tab.addInteracao(cmd2);
                return tab;
            }
        }
    }

    @Override
    public VlanBanda getVlanBanda(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = this.getCd().consulta(this.getComandoGetSrvc(i, "1"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        ComandoDslam cmd1 = this.getCd().consulta(this.getComandoGetSrvcStatus(i, 1));
        cmds.add(cmd1);
        List<String> pegaStatus = cmd1.getRetorno();
        String statusVlan = TratativaRetornoUtil.tratKeymile(pegaStatus, "MACSRCFilter");

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer svlan = new Integer("0");
        Integer cvlan = new Integer("0");
        EnumEstadoVlan state;

        if (!leSrvc.contentEquals("no service connected")) {
            ComandoDslam cmd2 = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc));
            cmds.add(cmd2);
            List<String> pegaVlan = cmd2.getRetorno();
            svlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        if (statusVlan.equalsIgnoreCase("None")) {
            state = EnumEstadoVlan.UP;
        } else if (statusVlan.equalsIgnoreCase("List")) {
            state = EnumEstadoVlan.DOWN;
        } else {
            state = EnumEstadoVlan.FLOODINGPREVENTION;
        }

        VlanBanda vlanBanda = new VlanBanda(cvlan, svlan, state);
        cmds.forEach(vlanBanda::addInteracao);

        return vlanBanda;
    }

    @Override
    public VlanVoip getVlanVoip(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = this.getCd().consulta(this.getComandoGetSrvc(i, "2"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        ComandoDslam cmd1 = this.getCd().consulta(this.getComandoGetSrvcStatus(i, 2));
        cmds.add(cmd1);
        List<String> pegaStatus = cmd1.getRetorno();
        String statusVlan = TratativaRetornoUtil.tratKeymile(pegaStatus, "MACSRCFilter");

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer svlan = new Integer("0");
        Integer cvlan = new Integer("0");
        EnumEstadoVlan state;
        if (!leSrvc.contentEquals("no service connected")) {
            ComandoDslam cmd2 = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc));
            cmds.add(cmd2);
            List<String> pegaVlan = cmd2.getRetorno();
            svlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        if (statusVlan.equalsIgnoreCase("None")) {
            state = EnumEstadoVlan.UP;
        } else if (statusVlan.equalsIgnoreCase("List")) {
            state = EnumEstadoVlan.DOWN;
        } else {
            state = EnumEstadoVlan.FLOODINGPREVENTION;
        }

        VlanVoip vlanVoip = new VlanVoip(cvlan, svlan, state);
        cmds.forEach(vlanVoip::addInteracao);
        return vlanVoip;
    }

    @Override
    public VlanVod getVlanVod(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = this.getCd().consulta(this.getComandoGetSrvc(i, "3"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        ComandoDslam cmd1 = this.getCd().consulta(this.getComandoGetSrvcStatus(i, 3));
        cmds.add(cmd1);
        List<String> pegaStatus = cmd1.getRetorno();
        List<String> pegaDetails = this.getCd().consulta(this.getComandoGetVlanVodPm(i)).getRetorno();

        String statusVlan = TratativaRetornoUtil.tratKeymile(pegaStatus, "MACSRCFilter");

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer svlan = new Integer("0");
        Integer cvlan = new Integer("0");
        EnumEstadoVlan state;

        if (!leSrvc.contentEquals("no service connected")) {
            ComandoDslam cmd2 = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc));
            cmds.add(cmd2);
            List<String> pegaVlan = cmd2.getRetorno();
            svlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        if (statusVlan.equalsIgnoreCase("None")) {
            state = EnumEstadoVlan.UP;
        } else if (statusVlan.equalsIgnoreCase("List")) {
            state = EnumEstadoVlan.DOWN;
        } else {
            state = EnumEstadoVlan.FLOODINGPREVENTION;
        }
        VlanVod vlanVod = new VlanVod(cvlan, svlan, state);
        try {
            vlanVod.setPctDown(new Integer(TratativaRetornoUtil.tratKeymile(pegaDetails, "Value", 3)));
            vlanVod.setPctUp(new Integer(TratativaRetornoUtil.tratKeymile(pegaDetails, "Value", 5)));
        } catch (Exception e) {
        }
        cmds.forEach(vlanVod::addInteracao);

        return vlanVod;
    }

    @Override
    public VlanMulticast getVlanMulticast(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = this.getCd().consulta(this.getComandoGetSrvc(i, "4"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        ComandoDslam cmd1 = this.getCd().consulta(this.getComandoGetSrvcStatus(i, 4));
        cmds.add(cmd1);
        List<String> pegaStatus = cmd1.getRetorno();
        ComandoDslam cmd2 = this.getCd().consulta(this.getComandoGetVlanMulticastPm(i));
        cmds.add(cmd2);
        List<String> pegaDetails = cmd2.getRetorno();
        ComandoDslam cmd3 = this.getCd().consulta(this.getComandoGetIpIgmp());
        cmds.add(cmd3);
        List<String> pegaIpIgmp = cmd3.getRetorno();

        String statusVlan = TratativaRetornoUtil.tratKeymile(pegaStatus, "MACSRCFilter");
        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");

        Integer svlan = new Integer("0");
//        Integer cvlan = new Integer("0");
        EnumEstadoVlan state;
        if (!leSrvc.contentEquals("no service connected")) {
            ComandoDslam cmd4 = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc));
            cmds.add(cmd4);
            List<String> pegaVlan = cmd4.getRetorno();
            svlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "McastVID"));
        }
        if (statusVlan.equalsIgnoreCase("None")) {
            state = EnumEstadoVlan.UP;
        } else if (statusVlan.equalsIgnoreCase("List")) {
            state = EnumEstadoVlan.DOWN;
        } else {
            state = EnumEstadoVlan.FLOODINGPREVENTION;
        }

        VlanMulticast vlanMult = new VlanMulticast(0, svlan, state);
        vlanMult.setPctDown(new Integer(TratativaRetornoUtil.tratKeymile(pegaDetails, "Value", 3)));
        vlanMult.setPctUp(new Integer(TratativaRetornoUtil.tratKeymile(pegaDetails, "Value", 5)));
        vlanMult.setIpIgmp(TratativaRetornoUtil.tratKeymile(pegaIpIgmp, "LocalIpAddressForIgmpGeneration"));
        cmds.forEach(vlanMult::addInteracao);
        return vlanMult;
    }

    @Override
    public Profile getProfile(InventarioRede i) throws Exception {
        ComandoDslam cmd = this.getCd().consulta(this.getProf(i));
        List<String> pegaProfile = cmd.getRetorno();
        String first = TratativaRetornoUtil.tratKeymile(pegaProfile, "Name");
        List<String> leProf = TratativaRetornoUtil.numberFromString(first);

        Profile prof = new ProfileMetalico();
        prof.setProfileDown(leProf.get(0));
        prof.setProfileUp(leProf.get(1));

        String profz = first.contains("_SUV") ? first : first + "_SUV";
        String leprofz = profz.contains("D1") ? profz.replace("D1", "") : profz;

        prof.setDown(compare(leprofz, Boolean.TRUE));
        prof.setUp(compare(leprofz, Boolean.FALSE));

        prof.addInteracao(cmd);

        return prof;
    }

    @Override
    public Modulacao getModulacao(InventarioRede ir) throws Exception {
        ComandoDslam cmd = this.getCd().consulta(this.getModul(ir));
        List<String> pegaModul = cmd.getRetorno();
        String modul = "";
        Integer i;
        for (i = 0; i < pegaModul.size(); i++) {
            if (pegaModul.get(i).contains("true")) {
                modul = pegaModul.get(i + 1).replaceAll("\\ # Name", "").replaceAll("\\\\", "").trim();
            }
        }

        Modulacao m = new Modulacao();
        m.setModulacao(modul);
        m.setModulEnum(compare(modul));
        m.addInteracao(cmd);

        return m;
    }

    @Override
    public Profile setProfileDown(InventarioRede i, Velocidades v) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = getCd().consulta(getComandoSetProfileDefault(i, v));
        cmds.add(cmd);
        String leSet = cmd.getBlob();
        if (leSet.contains("previously") || leSet.contains("is not compatible")) {
            ComandoDslam cmd1 = getCd().consulta(getComandoSetProfileSeco(i, v));
            cmds.add(cmd1);
            leSet = cmd1.getBlob();
            if (leSet.contains("previously") || leSet.contains("is not compatible")) {
                ComandoDslam cmd2 = getCd().consulta(getComandoSetProfileSUVD1(i, v));
                cmds.add(cmd2);
            }
        }
        Profile p = getProfile(i);
        for (int j = cmds.size() - 1; j >= 0; j--) {
            p.getInteracoes().add(0, cmds.get(j));
        }
        return p;
    }

    @Override
    public Profile setProfileUp(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
        return setProfileDown(i, vDown);
    }

    @Override
    public VlanBanda createVlanBanda(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanBanda(i));
        ComandoDslam cmd1 = getCd().consulta(getComandoSetMacSourceFilteringMode(i, "1", "none"));
        VlanBanda v = getVlanBanda(i);
        v.getInteracoes().add(0, cmd1);
        v.getInteracoes().add(0, cmd);
        return v;
    }

    @Override
    public VlanVoip createVlanVoip(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanVoip(i));
        ComandoDslam cmd1 = getCd().consulta(getComandoSetMacSourceFilteringMode(i, "2", "none"));
        VlanVoip v = getVlanVoip(i);
        v.getInteracoes().add(0, cmd1);
        v.getInteracoes().add(0, cmd);
        return v;
    }

    @Override
    public VlanVod createVlanVod(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanVod(i));
        ComandoDslam cmd1 = getCd().consulta(getComandoSetMacSourceFilteringMode(i, "3", "none"));
        VlanVod v = getVlanVod(i);
        v.getInteracoes().add(0, cmd1);
        v.getInteracoes().add(0, cmd);
        return v;
    }

    @Override
    public VlanMulticast createVlanMulticast(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanMulticast(i));
        ComandoDslam cmd1 = getCd().consulta(getComandoSetMacSourceFilteringMode(i, "4", "none"));
        VlanMulticast v = getVlanMulticast(i);
        v.getInteracoes().add(0, cmd1);
        v.getInteracoes().add(0, cmd);

        return v;
    }

    @Override
    public void resetIptvStatistics(InventarioRede i) throws Exception {
        getCd().consulta(getComandoResetMulticastStatistics(i));
        getCd().consulta(getComandoResetVodStatistics(i));
    }

    @Override
    public VlanBanda deleteVlanBanda(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        ComandoDslam cmd = getCd().consulta(getComandoGetSrvc(i, "1"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();

        List<String> leSrvc = TratativaRetornoUtil.numberFromString(TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected"));
        if (leSrvc.size() > 0) {
            String srvc = leSrvc.get(leSrvc.size() - 1).replace("-", "");
            cmds.add(getCd().consulta(getComandoDeleteVlan(srvc)));
        }

        VlanBanda v = getVlanBanda(i);

        for (int j = cmds.size() - 1;
                j >= 0; j--) {
            System.out.println("oi");
            v.getInteracoes().add(0, cmds.get(j));
        }
        return v;
    }

    @Override
    public VlanVoip deleteVlanVoip(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();

        ComandoDslam cmd = getCd().consulta(getComandoGetSrvc(i, "2"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        List<String> leSrvc = TratativaRetornoUtil.numberFromString(TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected"));
        if (leSrvc.size() > 0) {
            String srvc = leSrvc.get(leSrvc.size() - 1).replace("-", "");
            cmds.add(getCd().consulta(getComandoDeleteVlan(srvc)));
        }

        VlanVoip v = getVlanVoip(i);
        for (int j = cmds.size() - 1; j >= 0; j--) {
            v.getInteracoes().add(0, cmds.get(j));
        }
        return v;

    }

    @Override
    public VlanVod deleteVlanVod(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();

        ComandoDslam cmd = getCd().consulta(getComandoGetSrvc(i, "3"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        List<String> leSrvc = TratativaRetornoUtil.numberFromString(TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected"));
        if (leSrvc.size() > 0) {
            String srvc = leSrvc.get(leSrvc.size() - 1).replace("-", "");
            cmds.add(getCd().consulta(getComandoDeleteVlan(srvc)));
        }

        VlanVod v = getVlanVod(i);
        for (int j = cmds.size() - 1; j >= 0; j--) {
            v.getInteracoes().add(0, cmds.get(j));
        }
        return v;
    }

    @Override
    public VlanMulticast deleteVlanMulticast(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();

        ComandoDslam cmd = getCd().consulta(getComandoGetSrvc(i, "4"));
        cmds.add(cmd);
        List<String> pegaSrvc = cmd.getRetorno();
        List<String> leSrvc = TratativaRetornoUtil.numberFromString(TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected"));
        if (leSrvc.size() > 0) {
            String srvc = leSrvc.get(leSrvc.size() - 1).replace("-", "");
            cmds.add(getCd().consulta(getComandoDeleteMulticast(srvc)));
        }

        VlanMulticast v = getVlanMulticast(i);
        for (int j = cmds.size() - 1; j >= 0; j--) {
            v.getInteracoes().add(0, cmds.get(j));
        }
        return v;
    }

    @Override
    public Modulacao setModulacao(InventarioRede i, Velocidades v) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        String leResp = "";
        Boolean isAdsl = new Double(v.getVel()).compareTo(20d) <= 0;
        if (isAdsl) {
            ComandoDslam cmd = getCd().consulta(getComandoSetModulacaoAdsl(i, v));
            cmds.add(cmd);
            leResp = cmd.getBlob();
            if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                ComandoDslam cmd1 = getCd().consulta(getComandoSetModulacaoAdsl1(i, v));
                cmds.add(cmd1);
                leResp = cmd1.getBlob();
            }
        } else {
            ComandoDslam cmd = getCd().consulta(getComandoSetModulacaoVdsl(i, v));
            cmds.add(cmd);
            leResp = cmd.getBlob();
            if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                ComandoDslam cmd1 = getCd().consulta(getComandoSetModulacaoVdsl1(i, v));
                cmds.add(cmd1);
                leResp = cmd1.getBlob();
                if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                    ComandoDslam cmd2 = getCd().consulta(getComandoSetModulacaoVdsl11(i, v));
                    cmds.add(cmd2);
                    leResp = cmd2.getBlob();
                }
            }
        }

        Modulacao m = getModulacao(i);
        for (int j = cmds.size() - 1; j >= 0; j--) {
            m.getInteracoes().add(0, cmds.get(j));
        }

        return m;
    }

    @Override
    public DeviceMAC getDeviceMac(InventarioRede i) throws Exception {
        List<ComandoDslam> cmds = new ArrayList<>();
        cmds.add(getCd().consulta(getComandoGetDeviceMAC(i)));
        String macValue = "";
        for (int j = 0; j < 5; j++) {
            Thread.sleep(3000);
            ComandoDslam cmd = getCd().consulta(getComandoGetDeviceMAC1(i));
            cmds.add(cmd);
            macValue = TratativaRetornoUtil.tratKeymile(cmd.getRetorno(), "MacAddress");
            if (!macValue.contains("Parâmetro não encontrado")) {
                break;
            }
        }
        cmds.add(getCd().consulta(getComandoGetDeviceMAC2(i)));
        String comDoisPontos = "";
        try {
            comDoisPontos = macValue.substring(0, 2) + ":" + macValue.substring(2, 4) + ":" + macValue.substring(4, 6) + ":" + macValue.substring(6, 8)
                    + ":" + macValue.substring(8, 10) + ":" + macValue.substring(10, 12);
        } catch (Exception e) {
        }
        String leMac = comDoisPontos.isEmpty() ? macValue : comDoisPontos;
        DeviceMAC m = new DeviceMAC(leMac);
        cmds.forEach(m::addInteracao);
        return m;
    }

    protected ComandoDslam getComandoGetDeviceMAC(InventarioRede i) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-1/cfgm/macsourcefilteringmode floodingprevention");
    }

    protected ComandoDslam getComandoGetDeviceMAC1(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/status/one2onemacforwardinglist", 3000);
    }

    protected ComandoDslam getComandoGetDeviceMAC2(InventarioRede i) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-1/cfgm/macsourcefilteringmode none");
    }

    protected ComandoDslam getComandoCreateVlanMulticast(InventarioRede i) {
        return new ComandoDslam("cd /services/packet/mcast/cfgm", 1000,
                "createservice /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-4 {4000}");
    }

    protected ComandoDslam getComandoCreateVlanVod(InventarioRede i) {
        return new ComandoDslam("cd /services/packet/1to1doubletag/cfgm", 1000,
                "createservice /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-3 " + i.getCvlan() + " cos5 " + i.getVlanVod() + " cos5 swap");
    }

    protected ComandoDslam getComandoCreateVlanVoip(InventarioRede i) {
        return new ComandoDslam("cd /services/packet/1to1doubletag/cfgm", 1000,
                "createservice /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-2 " + i.getCvlan() + " cos3 " + i.getVlanVoip() + " cos3 swap");
    }

    protected ComandoDslam getComandoDeleteMulticast(String srvc) {
        return new ComandoDslam("cd /services/packet/mcast/cfgm/", 500, "deleteservice " + srvc);
    }

    protected ComandoDslam getComandoSetMacSourceFilteringMode(InventarioRede i, String intrf, String mode) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-" + intrf + "/cfgm/macsourcefilteringmode " + mode);
    }

    protected ComandoDslam getComandoCreateVlanBanda(InventarioRede i) {
        return new ComandoDslam("cd /services/packet/1to1doubletag/cfgm", 1000,
                "createservice /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-1 " + i.getCvlan() + " cos0 " + i.getRin() + " cos0 swap");
    }

    protected ComandoDslam getComandoDeleteVlan(String srvc) {
        return new ComandoDslam("cd /services/packet/1to1doubletag/cfgm/", 500, "deleteservice " + srvc);
    }

    protected ComandoDslam getComandoSetModulacaoAdsl(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set/unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "false default 0 false default 0 false default 0 true " + compare(v, Boolean.TRUE).getSintaxMod() + " priority");
    }

    protected ComandoDslam getComandoSetModulacaoAdsl1(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set/unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "false default 0 false default 0 false default 0 true ADSL2PLUS_SUVD11 priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set/unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + compare(v, Boolean.TRUE).getSintaxMod() + " 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl1(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set/unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + compare(v, Boolean.TRUE).getSintaxMod() + "D1 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl11(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set/unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + compare(v, Boolean.TRUE).getSintaxMod() + "D11 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetProfileDefault(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + compare(vDown, Boolean.TRUE).getSintaxVel());
    }

    protected ComandoDslam getComandoSetProfileSeco(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + compare(vDown, Boolean.TRUE).getSintaxVel().replace("_SUV", ""));
    }

    protected ComandoDslam getComandoSetProfileSUVD1(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + compare(vDown, Boolean.TRUE).getSintaxVel() + "D1");
    }

    protected ComandoDslam getModul(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles");
    }

    protected ComandoDslam getVelSinc(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/status/status");
    }

    protected ComandoDslam getSnrAtn(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/status/bandstatus");
    }

    protected ComandoDslam getComandoGetSrvc(InventarioRede i, String intrf) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-" + intrf + "/status/ServiceStatus", 3000);
    }

    protected ComandoDslam getComandoGetSrvcStatus(InventarioRede i, Integer intrf) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-" + intrf + "/cfgm/macsourcefilteringmode", 3000);
    }

    protected ComandoDslam getProf(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile", 3000);
    }

    protected ComandoDslam getComandoGetVlanVodPm(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-3/pm/usercountertable", 3000);
    }

    protected ComandoDslam getComandoGetVlanMulticastPm(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-4/pm/usercountertable", 3000);
    }

    protected ComandoDslam getComandoResetVodStatistics(InventarioRede i) {
        return new ComandoDslam("cd /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-3/pm\nusercounterreset");
    }

    protected ComandoDslam getComandoResetMulticastStatistics(InventarioRede i) {
        return new ComandoDslam("cd /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-4/pm\nusercounterreset");
    }
//    @Override

    public Profile castProfile(Velocidades v) {
        Profile p = new Profile();
        Double leVel = new Double(v.getVel());
        Double umDeUp = 20d;
        Double doisDeUp = 30d;
        Double tresDeUp = 40d;

        Boolean isUmDeUp = leVel.compareTo(umDeUp) <= 0;
        Boolean isDoisDeUp = leVel.compareTo(doisDeUp) <= 0;
        Boolean isTresDeUp = leVel.compareTo(tresDeUp) <= 0;

        if (isUmDeUp) {
            p.setProfileDown("HSI_" + v.getVel() + "Mb_1Mb_SUV");
            p.setProfileUp("HSI_" + v.getVel() + "Mb_1Mb");
        } else {
            if (isDoisDeUp) {
                p.setProfileDown("HSI_" + v.getVel() + "Mb_2Mb_SUV");
                p.setProfileUp("HSI_" + v.getVel() + "Mb_2Mb");
            } else {
                if (isTresDeUp) {
                    p.setProfileDown("HSI_" + v.getVel() + "Mb_3Mb_SUV");
                    p.setProfileUp("HSI_" + v.getVel() + "Mb_3Mb");
                } else {
                    p.setProfileDown("HSI_" + v.getVel() + "Mb_5Mb_SUV");
                    p.setProfileUp("HSI_" + v.getVel() + "Mb_5Mb");
                }
            }
        }

        return p;
    }

//    @Override
    public Modulacao castModulacao(Velocidades v) {
        Modulacao m = new Modulacao();

        Double leVel = new Double(v.getVel());
//        Double autoLimit = 5d;
        Double adslLimit = 20d;
//        Boolean isAuto = leVel.compareTo(autoLimit) <= 0;
        Boolean isAdsl = leVel.compareTo(adslLimit) <= 0;
        String leModul = isAdsl ? "ADSL2PLUS_ONLY_SUV" : "VDSL_17A_B8_12_SUV";
        m.setModulacao(leModul);

        return m;
    }
}
