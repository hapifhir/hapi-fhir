package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ParticipationFunctionEnumFactory implements EnumFactory<V3ParticipationFunction> {

  public V3ParticipationFunction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_AuthorizedParticipationFunction".equals(codeString))
      return V3ParticipationFunction._AUTHORIZEDPARTICIPATIONFUNCTION;
    if ("_AuthorizedReceiverParticipationFunction".equals(codeString))
      return V3ParticipationFunction._AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION;
    if ("AUCG".equals(codeString))
      return V3ParticipationFunction.AUCG;
    if ("AULR".equals(codeString))
      return V3ParticipationFunction.AULR;
    if ("AUTM".equals(codeString))
      return V3ParticipationFunction.AUTM;
    if ("AUWA".equals(codeString))
      return V3ParticipationFunction.AUWA;
    if ("_ConsenterParticipationFunction".equals(codeString))
      return V3ParticipationFunction._CONSENTERPARTICIPATIONFUNCTION;
    if ("GRDCON".equals(codeString))
      return V3ParticipationFunction.GRDCON;
    if ("POACON".equals(codeString))
      return V3ParticipationFunction.POACON;
    if ("PRCON".equals(codeString))
      return V3ParticipationFunction.PRCON;
    if ("PROMSK".equals(codeString))
      return V3ParticipationFunction.PROMSK;
    if ("SUBCON".equals(codeString))
      return V3ParticipationFunction.SUBCON;
    if ("_OverriderParticipationFunction".equals(codeString))
      return V3ParticipationFunction._OVERRIDERPARTICIPATIONFUNCTION;
    if ("AUCOV".equals(codeString))
      return V3ParticipationFunction.AUCOV;
    if ("AUEMROV".equals(codeString))
      return V3ParticipationFunction.AUEMROV;
    if ("_CoverageParticipationFunction".equals(codeString))
      return V3ParticipationFunction._COVERAGEPARTICIPATIONFUNCTION;
    if ("_PayorParticipationFunction".equals(codeString))
      return V3ParticipationFunction._PAYORPARTICIPATIONFUNCTION;
    if ("CLMADJ".equals(codeString))
      return V3ParticipationFunction.CLMADJ;
    if ("ENROLL".equals(codeString))
      return V3ParticipationFunction.ENROLL;
    if ("FFSMGT".equals(codeString))
      return V3ParticipationFunction.FFSMGT;
    if ("MCMGT".equals(codeString))
      return V3ParticipationFunction.MCMGT;
    if ("PROVMGT".equals(codeString))
      return V3ParticipationFunction.PROVMGT;
    if ("UMGT".equals(codeString))
      return V3ParticipationFunction.UMGT;
    if ("_SponsorParticipationFunction".equals(codeString))
      return V3ParticipationFunction._SPONSORPARTICIPATIONFUNCTION;
    if ("FULINRD".equals(codeString))
      return V3ParticipationFunction.FULINRD;
    if ("SELFINRD".equals(codeString))
      return V3ParticipationFunction.SELFINRD;
    if ("_UnderwriterParticipationFunction".equals(codeString))
      return V3ParticipationFunction._UNDERWRITERPARTICIPATIONFUNCTION;
    if ("PAYORCNTR".equals(codeString))
      return V3ParticipationFunction.PAYORCNTR;
    if ("REINS".equals(codeString))
      return V3ParticipationFunction.REINS;
    if ("RETROCES".equals(codeString))
      return V3ParticipationFunction.RETROCES;
    if ("SUBCTRT".equals(codeString))
      return V3ParticipationFunction.SUBCTRT;
    if ("UNDERWRTNG".equals(codeString))
      return V3ParticipationFunction.UNDERWRTNG;
    if ("ADMPHYS".equals(codeString))
      return V3ParticipationFunction.ADMPHYS;
    if ("ANEST".equals(codeString))
      return V3ParticipationFunction.ANEST;
    if ("ANRS".equals(codeString))
      return V3ParticipationFunction.ANRS;
    if ("ASSEMBLER".equals(codeString))
      return V3ParticipationFunction.ASSEMBLER;
    if ("ATTPHYS".equals(codeString))
      return V3ParticipationFunction.ATTPHYS;
    if ("COMPOSER".equals(codeString))
      return V3ParticipationFunction.COMPOSER;
    if ("DISPHYS".equals(codeString))
      return V3ParticipationFunction.DISPHYS;
    if ("FASST".equals(codeString))
      return V3ParticipationFunction.FASST;
    if ("MDWF".equals(codeString))
      return V3ParticipationFunction.MDWF;
    if ("NASST".equals(codeString))
      return V3ParticipationFunction.NASST;
    if ("PCP".equals(codeString))
      return V3ParticipationFunction.PCP;
    if ("PRISURG".equals(codeString))
      return V3ParticipationFunction.PRISURG;
    if ("REVIEWER".equals(codeString))
      return V3ParticipationFunction.REVIEWER;
    if ("RNDPHYS".equals(codeString))
      return V3ParticipationFunction.RNDPHYS;
    if ("SASST".equals(codeString))
      return V3ParticipationFunction.SASST;
    if ("SNRS".equals(codeString))
      return V3ParticipationFunction.SNRS;
    if ("TASST".equals(codeString))
      return V3ParticipationFunction.TASST;
    throw new IllegalArgumentException("Unknown V3ParticipationFunction code '"+codeString+"'");
  }

  public String toCode(V3ParticipationFunction code) {
    if (code == V3ParticipationFunction._AUTHORIZEDPARTICIPATIONFUNCTION)
      return "_AuthorizedParticipationFunction";
    if (code == V3ParticipationFunction._AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION)
      return "_AuthorizedReceiverParticipationFunction";
    if (code == V3ParticipationFunction.AUCG)
      return "AUCG";
    if (code == V3ParticipationFunction.AULR)
      return "AULR";
    if (code == V3ParticipationFunction.AUTM)
      return "AUTM";
    if (code == V3ParticipationFunction.AUWA)
      return "AUWA";
    if (code == V3ParticipationFunction._CONSENTERPARTICIPATIONFUNCTION)
      return "_ConsenterParticipationFunction";
    if (code == V3ParticipationFunction.GRDCON)
      return "GRDCON";
    if (code == V3ParticipationFunction.POACON)
      return "POACON";
    if (code == V3ParticipationFunction.PRCON)
      return "PRCON";
    if (code == V3ParticipationFunction.PROMSK)
      return "PROMSK";
    if (code == V3ParticipationFunction.SUBCON)
      return "SUBCON";
    if (code == V3ParticipationFunction._OVERRIDERPARTICIPATIONFUNCTION)
      return "_OverriderParticipationFunction";
    if (code == V3ParticipationFunction.AUCOV)
      return "AUCOV";
    if (code == V3ParticipationFunction.AUEMROV)
      return "AUEMROV";
    if (code == V3ParticipationFunction._COVERAGEPARTICIPATIONFUNCTION)
      return "_CoverageParticipationFunction";
    if (code == V3ParticipationFunction._PAYORPARTICIPATIONFUNCTION)
      return "_PayorParticipationFunction";
    if (code == V3ParticipationFunction.CLMADJ)
      return "CLMADJ";
    if (code == V3ParticipationFunction.ENROLL)
      return "ENROLL";
    if (code == V3ParticipationFunction.FFSMGT)
      return "FFSMGT";
    if (code == V3ParticipationFunction.MCMGT)
      return "MCMGT";
    if (code == V3ParticipationFunction.PROVMGT)
      return "PROVMGT";
    if (code == V3ParticipationFunction.UMGT)
      return "UMGT";
    if (code == V3ParticipationFunction._SPONSORPARTICIPATIONFUNCTION)
      return "_SponsorParticipationFunction";
    if (code == V3ParticipationFunction.FULINRD)
      return "FULINRD";
    if (code == V3ParticipationFunction.SELFINRD)
      return "SELFINRD";
    if (code == V3ParticipationFunction._UNDERWRITERPARTICIPATIONFUNCTION)
      return "_UnderwriterParticipationFunction";
    if (code == V3ParticipationFunction.PAYORCNTR)
      return "PAYORCNTR";
    if (code == V3ParticipationFunction.REINS)
      return "REINS";
    if (code == V3ParticipationFunction.RETROCES)
      return "RETROCES";
    if (code == V3ParticipationFunction.SUBCTRT)
      return "SUBCTRT";
    if (code == V3ParticipationFunction.UNDERWRTNG)
      return "UNDERWRTNG";
    if (code == V3ParticipationFunction.ADMPHYS)
      return "ADMPHYS";
    if (code == V3ParticipationFunction.ANEST)
      return "ANEST";
    if (code == V3ParticipationFunction.ANRS)
      return "ANRS";
    if (code == V3ParticipationFunction.ASSEMBLER)
      return "ASSEMBLER";
    if (code == V3ParticipationFunction.ATTPHYS)
      return "ATTPHYS";
    if (code == V3ParticipationFunction.COMPOSER)
      return "COMPOSER";
    if (code == V3ParticipationFunction.DISPHYS)
      return "DISPHYS";
    if (code == V3ParticipationFunction.FASST)
      return "FASST";
    if (code == V3ParticipationFunction.MDWF)
      return "MDWF";
    if (code == V3ParticipationFunction.NASST)
      return "NASST";
    if (code == V3ParticipationFunction.PCP)
      return "PCP";
    if (code == V3ParticipationFunction.PRISURG)
      return "PRISURG";
    if (code == V3ParticipationFunction.REVIEWER)
      return "REVIEWER";
    if (code == V3ParticipationFunction.RNDPHYS)
      return "RNDPHYS";
    if (code == V3ParticipationFunction.SASST)
      return "SASST";
    if (code == V3ParticipationFunction.SNRS)
      return "SNRS";
    if (code == V3ParticipationFunction.TASST)
      return "TASST";
    return "?";
  }

    public String toSystem(V3ParticipationFunction code) {
      return code.getSystem();
      }

}

